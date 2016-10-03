package config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("repository")
public class EntityManagerConfiguration {
    
    private static final String DEFAULT_JDBC_DRIVER = "org.h2.Driver";
    private static final String DEFAULT_JDBC_URL = "jdbc:h2:./test";
    private static final String DEFAULT_JDBC_USER = "sa";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        setDataSourceConfiguration(dataSource);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
      SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
      sessionFactory.setDataSource(dataSource());
      return sessionFactory.getObject();
    }

    private void setDataSourceConfiguration(DriverManagerDataSource dataSource) {
        dataSource.setDriverClassName(DEFAULT_JDBC_DRIVER);
        dataSource.setUrl(DEFAULT_JDBC_URL);
        dataSource.setUsername(DEFAULT_JDBC_USER);
    }
}
