package com.dico.listener;

import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * eureka服务中心事件监听类，可在该类中进行邮件发送，短信发送等进行服务的实时监听
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: EurekaStatusListener
 * 创建时间: 2018/12/7
 */
@Slf4j
@Component
public class EurekaStatusListener {

    /**
     * 服务下线监听方法
     *
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.info(event.getServerId() + "\t" + event.getAppName() + " 服务下线");
    }

    /**
     * 服务注册监听方法
     *
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        log.info(instanceInfo.getAppName() + "进行注册");
    }

    /**
     * 服务续约监听方法
     *
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info(event.getServerId() + "\t" + event.getAppName() + " 服务进行续约");
    }

    /**
     * 注册中心启动监听方法
     *
     * @param event
     */
    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        log.info("注册中心 启动");
    }

    /**
     * 注册中心启动完成监听方法
     *
     * @param event
     */
    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        log.info("Eureka Server 启动");
    }
}
