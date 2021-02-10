package com.scc363.hospitalproject.repositories;


import com.scc363.hospitalproject.datamodels.Log;
import com.scc363.hospitalproject.datamodels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface LogsRepository extends CrudRepository<Log, Integer> {
   Log findByUserName(String userName);
   Log findByLevel(String level);
   Log findByLevelAndUserName(String level, String userName);
   int countByLevelAndUserNameAndDate(String level, String userName, LocalDate date);
   int countByLevel(String level);
}
