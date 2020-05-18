package com.dico.jpa.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.dico.jpa.dao.JpaDao;
import com.dico.util.DynamicSearchUtils;
import com.dico.util.FilterBeanUtils;
import com.dico.util.ReflectUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("static-access")
public abstract class JpaService<T, K extends Serializable> implements Service<T, K>, ExampleService<T> {

    protected abstract JpaDao<T, K> getDao();

    @Override
    public void save(T model) {
        this.getDao().save(model);
    }

    @Override
    public void save(List<T> models) {
        this.getDao().saveAll(models);
    }

    @Override
    public void deleteById(K id) {
        this.getDao().deleteById(id);
    }

    @Override
    public void update(T model) {
        this.getDao().save(model);
    }

    @Override
    public List<T> findAll(T t) {
        Example<T> example = Example.of(t);
        return this.getDao().findAll(example);
    }

    public List<Map<String, Object>> findAll(T t, HttpServletRequest request) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Example<T> example = Example.of(t);
        FilterBeanUtils<T> filterBeanUtils = new FilterBeanUtils<T>();
        return filterBeanUtils.filterBeans(this.getDao().findAll(example), request);
    }

    public List<T> findAll(T t, Sort sort) {
        Example<T> example = Example.of(t);
        return this.getDao().findAll(example, sort);
    }

    public List<Map<String, Object>> findAll(T t, Sort sort, HttpServletRequest request) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Example<T> example = Example.of(t);
        FilterBeanUtils<T> filterBeanUtils = new FilterBeanUtils<T>();
        return filterBeanUtils.filterBeans(this.getDao().findAll(example, sort), request);
    }

    public Page<T> findAll(T t, int pageNum, int pageSize, Sort sort) {
        Pageable page = PageRequest.of(pageNum, pageSize, sort);
        Example<T> example = Example.of(t);
        return this.getDao().findAll(example, page);
    }

    @Override
    public Page<T> findAll(T t, int pageNum, int pageSize) {
        Pageable page = PageRequest.of(pageNum, pageSize);
        Example<T> example = Example.of(t);
        return this.getDao().findAll(example, page);
    }

    public T findOne(K id) {
        return this.getDao().getOne(id);
    }

    public Page<T> findAll(Map<String, Object> params, int pageNum, int pageSize) {
        Specification<T> spec = DynamicSearchUtils.getSpecification(params, ReflectUtils.findParameterizedType(getClass(), 0));
        Pageable page = PageRequest.of(pageNum, pageSize);
        return getDao().findAll(spec, page);
    }

    public Page<T> findAll(Map<String, Object> params, int pageNum, int pageSize, Sort sort) {
        Specification<T> spec = DynamicSearchUtils.getSpecification(params, ReflectUtils.findParameterizedType(getClass(), 0));
        Pageable page = PageRequest.of(pageNum, pageSize, sort);
        return getDao().findAll(spec, page);
    }

    public List<T> findAll(Map<String, Object> params) {
        Specification<T> spec = DynamicSearchUtils.getSpecification(params, ReflectUtils.findParameterizedType(getClass(), 0));
        return this.getDao().findAll(spec);
    }

    public List<Map<String, Object>> findAll(Map<String, Object> params, HttpServletRequest request) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        FilterBeanUtils<T> filterBeanUtils = new FilterBeanUtils<T>();
        Specification<T> spec = DynamicSearchUtils.getSpecification(params, ReflectUtils.findParameterizedType(getClass(), 0));
        return filterBeanUtils.filterBeans(this.getDao().findAll(spec), request);
    }

    public List<T> findAll(Map<String, Object> params, Sort sort) {
        Specification<T> spec = DynamicSearchUtils.getSpecification(params, ReflectUtils.findParameterizedType(getClass(), 0));
        return this.getDao().findAll(spec, sort);
    }

    public List<Map<String, Object>> findAll(Map<String, Object> params, Sort sort, HttpServletRequest request) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        FilterBeanUtils<T> filterBeanUtils = new FilterBeanUtils<T>();
        Specification<T> spec = DynamicSearchUtils.getSpecification(params, ReflectUtils.findParameterizedType(getClass(), 0));
        return filterBeanUtils.filterBeans(this.getDao().findAll(spec, sort), request);
    }

}
