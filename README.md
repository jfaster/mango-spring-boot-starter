# mango-spring-boot-starter

# 概要
Mango针对springboot提供的starter

# 例子
>  * JDK 1.7 or above
>  * 编译工具 [Maven][maven] or [Gradle][gradle]

## 添加依赖
   ```xml
    <dependency>
        <groupId>org.jfaster</groupId>
        <artifactId>mango-spring-boot-starter</artifactId>
        <version>0.4</version>
    </dependency>
   ```
   
## 配置yml或者properties文件

    src/main/resources/application.yml

    mango:
      #dao所在的基包，多个包用逗号分割
      scan-package: org.jfster.mango.dao
      #mango引用数据源名配置，ds1和ds2..是数据源的key，可以自定义，如果没有slave，可以不配置。
      #mango中的连接池使用hikaricp，所以hikaricp中的配置在此都可以配置。
      datasources:
        - name: ds1
          master:
            driver-class-name: com.mysql.jdbc.Driver
            jdbc-url: jdbc:mysql://127.0.0.1:3306/test
            user-name: root
            password: 272777475
            maximum-pool-size: 10
            connection-timeout: 3000
    
          #slaves:
          #  - driver-class-name: com.mysql.jdbc.Driver
          #    jdbc-url: jdbc:mysql://127.0.0.1:3306/test
          #    user-name: root
          #    password: 272777475
          #    maximum-pool-size: 10
          #    connection-timeout: 3000
    
        #如果不想使用内置的连接池，在此指定其他数据源的bean
          #  - ref: 其他数据源在spring中的id
    
        #- name: ds2
        #  master:
        #    driver-class-name: com.mysql.jdbc.Driver
        #    jdbc-url: jdbc:mysql://127.0.0.1:3306/test
        #    user-name: root
        #    password: 272777475
        #    maximum-pool-size: 10
        #    connection-timeout: 3000
      #获取mango实例工厂，默认是org.jfaster.mango.plugin.spring.DefaultMangoFactoryBean，从spring容器中获取mango实例
      #默认就可以，如果有特殊需要可以指定自己的mango工厂类，继承org.jfaster.mango.plugin.spring.AbstractMangoFactoryBean实现createMango()方法
      #factory-class: org.jfaster.mango.plugin.spring.DefaultMangoFactoryBean
    
      #是否兼容空list
      #compatible-with-empty-list: true
    
      #是否检查列
      #check-column: false
    
      #是否方法参数使用真名，jdk1.8以后才支持
      #use-actual-param-name: false
    
      #批量更新是否都执行完提交事务
      #use-transaction-for-batch-update: false
    
      #缓存处理器
      #cache-handler: 实现org.jfaster.mango.operator.cache.CacheHandler的缓存处理器的类路径
    
      #是否懒加载
      #lazy-init: false
    
      #mango拦截器，实现org.jfaster.mango.interceptor.Interceptor方法，可以配置多个
      #interceptors:
      #  - 拦截器1的类全限定型名称
      #  - 拦截器2的类全限定型名称
      #  - 拦截器2的类全限定型名称
    

      src/main/resources/application.properties

      #dao所在的基包，多个包用逗号分割
      mango.scan-package=org.jfster.mango.dao
      
      #mango引用数据源名配置，ds1和ds2..是数据源的key，可以自定义，如果没有slave，可以不配置。
      #mango中的连接池使用hikaricp，所以hikaricp中的配置在此都可以配置。      
      mango.datasources[0].name=ds1
      mango.datasources[0].master.driver-class-name=com.mysql.jdbc.Driver
      mango.datasources[0].master.jdbc-url=jdbc:mysql://127.0.0.1:3306/test
      mango.datasources[0].master.user-name=root
      mango.datasources[0].master.password=272777475
      mango.datasources[0].master.maximum-pool-size=10
      mango.datasources[0].master.connection-timeout=3000
      
      #mango.datasources[0].slaves[0].driver-class-name=com.mysql.jdbc.Driver
      #mango.datasources[0].slaves[0].jdbc-url-class-name=jdbc:mysql://127.0.0.1:3306/test
      #mango.datasources[0].slaves[0].user-name=root
      #mango.datasources[0].slaves[0].password=272777475
      #mango.datasources[0].slaves[0].maximum-pool-size=10
      #mango.datasources[0].slaves[0].connection-timeout=3000
      
      #如果不想使用内置的连接池，在此指定其他数据源的bean。
      #mango.datasource[0].slaves[1].ref=其他数据源在spring中的id
      
      #mango.datasources[1].name=ds2
      #mango.datasources[1].master.driver-class-name=com.mysql.jdbc.Driver
      #mango.datasources[1].master.jdbc-url-class-name=jdbc:mysql://127.0.0.1:3306/test
      #mango.datasources[1].master.user-name=root
      #mango.datasources[1].master.password=272777475
      #mango.datasources[1].master.maximum-pool-size=10
      #mango.datasources[1].master.connection-timeout=3000
      
      #获取mango实例工厂，默认是org.jfaster.mango.plugin.spring.DefaultMangoFactoryBean，从spring容器中获取mango实例
      #默认就可以，如果有特殊需要可以指定自己的mango工厂类，继承org.jfaster.mango.plugin.spring.AbstractMangoFactoryBean实现createMango()方法
      #mango.factory-class=org.jfaster.mango.plugin.spring.DefaultMangoFactoryBean
      
      #是否兼容空list
      #mango.compatible-with-empty-list=true
      
      #是否检查列
      #mango.check-column=false
      
      #是否方法参数使用真名，jdk1.8以后才支持
      #mango.use-actual-param-name=false
      
      #批量更新是否都执行完提交事务
      #mango.use-transaction-for-batch-update=false
      
      #缓存处理器
      #mango.cache-handler=实现org.jfaster.mango.operator.cache.CacheHandler的缓存处理器的类路径
      
      #是否懒加载
      #mango.lazy-init=false
      
      #mango拦截器，实现org.jfaster.mango.interceptor.Interceptor方法，可以配置多个
      #mango.interceptors[0]=拦截器1的类全限定型名称
      #mango.interceptors[1]=拦截器2的类全限定型名称
      #mango.interceptors[2]=拦截器2的类全限定型名称

注释部分不是必须配置项，可以根据自己需要自行配置。

## 创建dao，并启动应用
 
1.
    
    `src/main/java/org/jfaster/mango/pojo/User.java`

    ```java
    package org.jfster.mango.pojo;
    
    /**
     * @author fangyanpeng.
     */
    public class User {
    
        private int id;
    
        private String name;
    
        private int age;
    
        public int getId() {
            return id;
        }
    
        public void setId(int id) {
            this.id = id;
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public int getAge() {
            return age;
        }
    
        public void setAge(int age) {
            this.age = age;
        }
    }

    ```

    `src/main/java/org/jfaster/mango/dao/UserDao.java`

    ```java
    package org.jfster.mango.dao;
    
    import org.jfaster.mango.annotation.DB;
    import org.jfaster.mango.annotation.SQL;
    import org.jfster.mango.pojo.User;
    
    import java.util.List;
    
    /**
     * 
     * @author fangyanpeng.
     */
    @DB(table = "distinct_test", name = "ds1")
    public interface UserDao {
    
        @SQL("select id,name,age from #table")
        List<User> getAllUsers();
    }

    ```

2. 

   `src/main/java/org/jfaster/mango/TestApplication.java`

   ```java

   package org.jfster.mango;
   
   import org.jfster.mango.dao.UserDao;
   import org.jfster.mango.pojo.User;
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.context.ApplicationContext;
   
   import java.util.List;
   
   @SpringBootApplication
   public class TestApplication {
   
   	public static void main(String[] args) {
   		ApplicationContext context = SpringApplication.run(TestApplication.class, args);
   		UserDao userDao = context.getBean(UserDao.class);
   		List<User> users = userDao.getAllUsers();
   		for (User user : users){
   			System.out.println(user.getId() + "|" + user.getName() + "|" + user.getAge());
   		}
   	}
   }
   ```
