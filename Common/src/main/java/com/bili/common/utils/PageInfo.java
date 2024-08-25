package com.bili.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo<T> {
    private List<T> records;
    private Long total;

    public static<T> PageInfo<T> restPage(IPage<T> page) {
        PageInfo<T> result = new PageInfo<T>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        return result;
    }

    public static<T> PageInfo<T> restPage(Page<T> page) {
        PageInfo<T> result = new PageInfo<T>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        return result;
    }
}
