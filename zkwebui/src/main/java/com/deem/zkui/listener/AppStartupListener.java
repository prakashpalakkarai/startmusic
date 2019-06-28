package com.deem.zkui.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.deem.zkui.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

@WebListener
public class AppStartupListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(AppStartupListener .class);

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
         logger.info("contextDestroyed...");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
       logger.info("contextInitialized ...");
         Properties globalProps = new Properties();
         File f = new File("C:/Users/ppalakkarai/sw/java/apache-tomcat-8.5.42/lib/config.cfg");

         try(FileInputStream fis = new FileInputStream(f)) {
             if (f.exists()) {
                 globalProps.load(fis);
             } else {
                 logger.error("Please create config.cfg properties file and then execute the program!");
                 throw new RuntimeException("config.cfg not found");
             }

             globalProps.setProperty("uptime", new Date().toString());
             new Dao(globalProps).checkNCreate();

             servletContextEvent.getServletContext().setAttribute("globalProps", globalProps);
         } catch(Exception ex) {
             logger.error("contextInitialized: error while loading config properties. " + ex.getMessage() + " Rethrowing as Runtime Exception.");
             throw new RuntimeException(ex);
         }
    }
}