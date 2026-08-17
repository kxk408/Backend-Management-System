package com.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.mapper.DeptMapper;
import com.backend.pojo.Dept;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class DeptServiceImpl implements DeptService {

    private static final String DEPT_LIST_KEY = "backend:dept:list";
    private static final String DEPT_ID_KEY_PREFIX = "backend:dept:id:";
    /** 按 id 查不到时的占位值：与「key 不存在(miss)」区分 */
    private static final String EMPTY_MARKER = "__EMPTY__";

    /** 正常命中：300s 基础 + [0,60] 随机抖动，缓解雪崩 */
    private static final long TTL_BASE_SECONDS = 300;
    private static final int TTL_JITTER_SECONDS = 60;
    /** 空值短 TTL，防穿透同时尽快恢复「后来有数据」 */
    private static final Duration EMPTY_TTL = Duration.ofSeconds(45);

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Dept> findAll() {
        try {
            Object cached = redisTemplate.opsForValue().get(DEPT_LIST_KEY);
            if (cached != null) {
                List<Dept> list = objectMapper.convertValue(cached, new TypeReference<List<Dept>>() {});
                if (list == null || list.isEmpty()) {
                    log.info("dept list cache hit empty, key={}", DEPT_LIST_KEY);
                    return list == null ? Collections.emptyList() : list;
                }
                log.info("dept list cache hit, key={}", DEPT_LIST_KEY);
                return list;
            }
            log.info("dept list cache miss, key={}", DEPT_LIST_KEY);
        } catch (Exception e) {
            log.warn("dept list cache read failed, fallback to db, key={}", DEPT_LIST_KEY, e);
        }

        List<Dept> list = deptMapper.findAll();
        writeDeptListCache(list);
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public void deleteDept(int id) {
        deptMapper.deleteDept(id);
        evictDeptCaches(id);
    }

    @Override
    public void addDept(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.addDept(dept);
        // 新增后列表（含可能的空列表占位）必须失效
        evictDeptListCache();
    }

    @Override
    public Dept getById(Integer id) {
        String key = deptIdKey(id);
        try {
            Object cached = redisTemplate.opsForValue().get(key);
            if (cached != null) {
                if (isEmptyMarker(cached)) {
                    log.info("dept id cache hit empty, key={}", key);
                    return null;
                }
                log.info("dept id cache hit, key={}", key);
                return objectMapper.convertValue(cached, Dept.class);
            }
            log.info("dept id cache miss, key={}", key);
        } catch (Exception e) {
            log.warn("dept id cache read failed, fallback to db, key={}", key, e);
        }

        Dept dept = deptMapper.getById(id);
        writeDeptIdCache(key, dept);
        return dept;
    }

    @Override
    public void changeName(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
        evictDeptCaches(dept.getId());
    }

    private void writeDeptListCache(List<Dept> list) {
        try {
            if (list == null || list.isEmpty()) {
                // 穿透：空列表也写入，短 TTL；值用 []，与 miss(null) 区分
                redisTemplate.opsForValue().set(DEPT_LIST_KEY, Collections.emptyList(), EMPTY_TTL);
                log.info("dept list cache written empty, key={}, ttl={}s", DEPT_LIST_KEY, EMPTY_TTL.toSeconds());
            } else {
                Duration ttl = normalTtlWithJitter();
                redisTemplate.opsForValue().set(DEPT_LIST_KEY, list, ttl);
                log.info("dept list cache written, key={}, ttl={}s", DEPT_LIST_KEY, ttl.toSeconds());
            }
        } catch (Exception e) {
            log.warn("dept list cache write failed, key={}", DEPT_LIST_KEY, e);
        }
    }

    private void writeDeptIdCache(String key, Dept dept) {
        try {
            if (dept == null) {
                // 穿透：查无也缓存占位常量，短 TTL
                redisTemplate.opsForValue().set(key, EMPTY_MARKER, EMPTY_TTL);
                log.info("dept id cache written empty, key={}, ttl={}s", key, EMPTY_TTL.toSeconds());
            } else {
                Duration ttl = normalTtlWithJitter();
                redisTemplate.opsForValue().set(key, dept, ttl);
                log.info("dept id cache written, key={}, ttl={}s", key, ttl.toSeconds());
            }
        } catch (Exception e) {
            log.warn("dept id cache write failed, key={}", key, e);
        }
    }

    /** 雪崩：基础 TTL + 随机抖动，避免同一时刻集体过期 */
    private Duration normalTtlWithJitter() {
        int jitter = ThreadLocalRandom.current().nextInt(TTL_JITTER_SECONDS + 1);
        return Duration.ofSeconds(TTL_BASE_SECONDS + jitter);
    }

    private String deptIdKey(Integer id) {
        return DEPT_ID_KEY_PREFIX + id;
    }

    private boolean isEmptyMarker(Object cached) {
        return EMPTY_MARKER.equals(cached) || EMPTY_MARKER.equals(String.valueOf(cached));
    }

    private void evictDeptListCache() {
        Boolean deleted = redisTemplate.delete(DEPT_LIST_KEY);
        log.info("dept list cache evicted, key={}, deleted={}", DEPT_LIST_KEY, deleted);
    }

    private void evictDeptCaches(Integer id) {
        evictDeptListCache();
        if (id != null) {
            String key = deptIdKey(id);
            Boolean deleted = redisTemplate.delete(key);
            log.info("dept id cache evicted, key={}, deleted={}", key, deleted);
        }
    }
}
