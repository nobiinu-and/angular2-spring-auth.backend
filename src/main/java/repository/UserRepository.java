package repository;

import entity.Authority;
import entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface UserRepository {

    @Select("SELECT id, " +
            " name, " +
            " hashedPassword " +
            "FROM user " +
            "WHERE name=#{name} "
            )
    @Results({
        @Result(property = "id", column = "id"), 
        @Result(property = "name", column = "name"), 
        @Result(property = "hashedPassword", column = "hashedPassword"), 
        @Result(property = "authorities", javaType = List.class, column = "id",
                many = @Many(select = "internalGetAuthorityForUser"))})
    User findOneByName(String name);

    @Select("SELECT id, " +
            " name, " +
            " hashedPassword " +
            "FROM user " +
            "WHERE id=#{id} "
            )
    @Results({
        @Result(property = "id", column = "id"), 
        @Result(property = "name", column = "name"), 
        @Result(property = "hashedPassword", column = "hashedPassword"), 
        @Result(property = "authorities", javaType = List.class, column = "id",
                many = @Many(select = "internalGetAuthorityForUser"))})
    User findOne(String id);


    @Select("SELECT id, " +
            " name, " +
            " hashedPassword " +
            "FROM user "
            )
    List<User> findAll();
    
    @Select("SELECT name FROM user_authority WHERE userid = #{id}")
    Authority internalGetAuthorityForUser(@Param("id") String id);
}
