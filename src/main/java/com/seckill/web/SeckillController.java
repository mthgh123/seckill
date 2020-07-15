package com.seckill.web;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.dto.SeckillResult;
import com.seckill.entity.Seckill;
import com.seckill.enums.SeckillStatEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/seckill") //url:/模块/资源/{id}/细分    如：/seckill/list
public class SeckillController {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    //获取秒杀的列表页
    @GetMapping("/list")
    public String list(HttpServletRequest request){
        //获取列表页
        List<Seckill> list=seckillService.getSeckillList();
        request.setAttribute("list",list);
        return "list";
    }

    //详情页
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(HttpServletRequest request, @PathVariable("seckillId") Long seckillId){
        if(seckillId ==null ){
            return "redirect:/seckill/list";
        }
        Seckill seckill=seckillService.getById(seckillId);
        if(seckill == null){
            return "forward:/seckill/list";
        }
        //排除掉上述两种情况之后
        request.setAttribute("seckill",seckill);
        return "detail";
    }

    //ajax json
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody  //看到此注解会将返回类型封装成json
    public SeckillResult<Exposer> exposer(Long seckillId){
        SeckillResult<Exposer> result;
        try{
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            result=new SeckillResult<>(true,exposer);

        } catch (Exception e){
            logger.error(e.getMessage(),e);
            result=new SeckillResult<>(false,e.getMessage());
        }
        return result;
    }

    //执行秒杀
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone",required = false) Long phone){
        if(phone == null){
            return new SeckillResult<>(false,"未注册");
        }
        try{
            SeckillExecution execution=seckillService.executeSeckill(seckillId,phone,md5);
            return new SeckillResult<>(true,execution);
        } catch (RepeatKillException e){
            SeckillExecution execution=new SeckillExecution(seckillId, -1,"重复秒杀");
            return new SeckillResult<>(false,execution);
        } catch (SeckillCloseException e){
            SeckillExecution execution=new SeckillExecution(seckillId, -2,"秒杀结束");
            return new SeckillResult<>(false,execution);
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            //除了上述的两种异常之外的所有其他异常
            SeckillExecution execution=new SeckillExecution(seckillId, 0,"内部错误");
            return new SeckillResult<>(false,execution);
        }
    }

    //获取系统时间
    @RequestMapping(value = "/time/now", method=RequestMethod.GET)
    public SeckillResult<Long> time(){
        //new Date() 初始化为系统的当前时间
        Date now = new Date();
        return new SeckillResult(true,now.getTime());
    }
}
