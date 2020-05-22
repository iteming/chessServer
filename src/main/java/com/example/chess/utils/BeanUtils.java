package com.example.chess.utils;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.dozer.loader.api.TypeMappingOptions.mapEmptyString;
import static org.dozer.loader.api.TypeMappingOptions.mapNull;


/**
 * @version 1.0.0
 * @author: jiangbin
 * @date 2018/3/1
 * @description 对象复制
 **/
public class BeanUtils {

    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static DozerBeanMapper dozerBeanMapper ;

    public static DozerBeanMapper getDozerBeanMapper() {
        if(dozerBeanMapper == null){
            dozerBeanMapper = new DozerBeanMapper();
        }
        return dozerBeanMapper;
    }

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        return getDozerBeanMapper().map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = new ArrayList<>();
        for (Object sourceObject : sourceList) {
            T destinationObject = getDozerBeanMapper().map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中.
     */
    public static void copy(Object source, Object destinationObject) {
        getDozerBeanMapper().map(source, destinationObject);
    }


    /**
     * 非空拷贝
     * @param sources 源数据对象
     * @param destination 目标数据对象
     */
    public static void copyWithOutEmpty(final Object sources, final Object destination){
        WeakReference weakReference = new WeakReference(getDozerBeanMapper());
        DozerBeanMapper mapper = (DozerBeanMapper) weakReference.get();
        mapper.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(sources.getClass(), destination.getClass(), mapNull(false), mapEmptyString(false));
            }
        });
        mapper.map(sources, destination);
        mapper.destroy();
        weakReference.clear();
    }
}
