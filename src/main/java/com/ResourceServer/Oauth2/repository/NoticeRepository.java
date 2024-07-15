package com.ResourceServer.Oauth2.repository;


import com.ResourceServer.Oauth2.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long> {

    // curdate() == current data i.e this is saying get all notices that start and end in between currend date. currDate().
    @Query(value="from Notice n where CURDATE() BETWEEN noticBegDt AND noticEndDt")
    List<Notice> findAllActiveNotices();
}
