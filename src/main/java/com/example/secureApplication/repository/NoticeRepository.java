package com.example.secureApplication.repository;

import com.example.secureApplication.model.Notice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Long> {
    @Query(value = "from Notice n where CURDATE()  between noticBegDt and noticEndDt")
    List<Notice> findAllActiveNotice();
}
