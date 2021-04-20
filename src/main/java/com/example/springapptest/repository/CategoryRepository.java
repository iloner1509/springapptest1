package com.example.springapptest.repository;

import com.example.springapptest.model.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category,Integer> {
    Category findByName(String Name);

//    @Transactional
//    @Modifying
//    @Query("update Category c set c.parent = null where c.parent = :id")
//    void updateParentIdToNull(@Param("id") Integer id);
//
//    @Transactional
//    @Modifying
//    @Query(value = "update categories set parent_id = null",nativeQuery = true)
//    void updateParentIdToNull();
//
//    @Transactional
//    @Modifying
//    @Query(value = "delete from categories",nativeQuery = true)
//    void deleteAllCategory();
}
