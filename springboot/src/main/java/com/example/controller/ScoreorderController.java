package com.example.controller;

import com.example.common.Result;
import com.example.entity.Scoreorder;
import com.example.service.ScoreorderService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 积分兑换表前端操作接口
 **/
@RestController
@RequestMapping("/scoreorder")
public class ScoreorderController {

    @Resource
    private ScoreorderService scoreorderService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Scoreorder scoreorder) {
        scoreorderService.add(scoreorder);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        scoreorderService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        scoreorderService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Scoreorder scoreorder) {
        scoreorderService.updateById(scoreorder);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Scoreorder scoreorder = scoreorderService.selectById(id);
        return Result.success(scoreorder);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Scoreorder scoreorder ) {
        List<Scoreorder> list = scoreorderService.selectAll(scoreorder);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Scoreorder scoreorder,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Scoreorder> page = scoreorderService.selectPage(scoreorder, pageNum, pageSize);
        return Result.success(page);
    }

}