package com.atguigu.cloud.controller;


import cn.hutool.core.date.DateUtil;
import com.atguigu.cloud.apis.PayFeignApi;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @auther zzyy
 * @create 2023-11-04 16:00
 */
@RestController
public class OrderController{

    public static final String PaymentSrv_URL = "http://cloud-payment-service";//服务注册中心上的微服务名称
//    @Autowired
//    private RestTemplate restTemplate;
    @Resource
    private PayFeignApi payFeignApi;

    /**
     * 一般情况下，通过浏览器的地址栏输入url，发送的只能是get请求
     * 我们底层调用的是post方法，模拟消费者发送get请求，客户端消费者
     * 参数可以不添加@RequestBody
     * @param payDTO
     * @return
     */
    @GetMapping("/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO){
//        return restTemplate.postForObject(PaymentSrv_URL + "/pay/add",payDTO,ResultData.class);
        return payFeignApi.addPay(payDTO);
    }
    // 删除+修改操作作为家庭作业，O(∩_∩)O。。。。。。。
    @GetMapping("/feign/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id)
    {
        System.out.println("-------支付微服务远程调用，按照id查询订单支付流水信息");
        ResultData resultData = null;
        try
        {
            System.out.println("调用开始-----:"+DateUtil.now());
            resultData = payFeignApi.getPayInfo(id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用结束-----:"+DateUtil.now());
            ResultData.fail(ReturnCodeEnum.RC500.getCode(),e.getMessage());
        }
        return resultData;
    }



    @GetMapping(value = "/consumer/pay/get/info")
    private String getInfoByConsul()
    {
//        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/info", String.class);
        try
        {
            System.out.println("调用开始-----:"+DateUtil.now());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用结束-----:"+ DateUtil.now());
            ResultData.fail(ReturnCodeEnum.RC500.getCode(),e.getMessage());
        }
        return payFeignApi.mylb();
    }

    /**
     * Resilience4j CircuitBreaker 的例子
     * @param id
     * @return
     */


}