package com.example.controller;

import com.example.common.Result;
import com.example.entity.Fileorder;
import com.example.service.FileorderService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资料订单表前端操作接口
 **/
@RestController
@RequestMapping("/fileorder")
public class FileorderController {

    @Resource
    private FileorderService fileorderService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Fileorder fileorder) {
        fileorderService.add(fileorder);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        fileorderService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        fileorderService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Fileorder fileorder) {
        fileorderService.updateById(fileorder);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Fileorder fileorder = fileorderService.selectById(id);
        return Result.success(fileorder);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Fileorder fileorder ) {
        List<Fileorder> list = fileorderService.selectAll(fileorder);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Fileorder fileorder,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Fileorder> page = fileorderService.selectPage(fileorder, pageNum, pageSize);
        return Result.success(page);
    }

}