package com.example.controller;

import com.example.common.Result;
import com.example.entity.Signin;
import com.example.service.SigninService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 嵌套信息表前端操作接口
 **/
@RestController
@RequestMapping("/signin")
public class SigninController {

    @Resource
    private SigninService signinService;

    /**
     * 签到
     */
    @PostMapping("/add")
    public Result add(@RequestBody Signin signin) {
        signinService.add(signin);
        return Result.success();
    }

    @GetMapping("/selectByUserId")
    public Result selectByUserId(@RequestParam Integer id) {
        Signin signin = signinService.selectByUserId(id);
        return Result.success(signin);
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        signinService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        signinService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Signin signin) {
        signinService.updateById(signin);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Signin signin = signinService.selectById(id);
        return Result.success(signin);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Signin signin ) {
        List<Signin> list = signinService.selectAll(signin);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Signin signin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Signin> page = signinService.selectPage(signin, pageNum, pageSize);
        return Result.success(page);
    }

}