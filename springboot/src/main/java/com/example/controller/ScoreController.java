package com.example.controller;

import com.example.common.Result;
import com.example.entity.Score;
import com.example.service.ScoreService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 积分专区表前端操作接口
 **/
@RestController
@RequestMapping("/score")
public class ScoreController {

    @Resource
    private ScoreService scoreService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Score score) {
        scoreService.add(score);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        scoreService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        scoreService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Score score) {
        scoreService.updateById(score);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Score score = scoreService.selectById(id);
        return Result.success(score);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Score score ) {
        List<Score> list = scoreService.selectAll(score);
        return Result.success(list);
    }

    @GetMapping("/getRecommend")
    public Result getRecommend() {
        Score score = scoreService.getRecommend();
        return Result.success(score);
    }

    @GetMapping("/getTop8")
    public Result getTop8() {
        List<Score> list = scoreService.getTop8();
        return Result.success(list);
    }


    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Score score,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Score> page = scoreService.selectPage(score, pageNum, pageSize);
        return Result.success(page);
    }

}