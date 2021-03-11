package com.webperside.namazvaxtlaribot.repository;

import com.webperside.namazvaxtlaribot.enums.ActionLogStatus;
import com.webperside.namazvaxtlaribot.models.ActionLog;
import com.webperside.namazvaxtlaribot.telegram.enums.TelegramCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Integer>,
        JpaSpecificationExecutor<ActionLog> {

    default Page<ActionLog> findAllBySearchParams(Pageable pageable, Map<String, String> searchParams){
        return findAll((root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(searchParams != null && !searchParams.isEmpty()){
                if(searchParams.get(ActionLog.Fields.user) != null){
                    predicates.add(cb.equal(root.get(ActionLog.Fields.user),
                            searchParams.get(ActionLog.Fields.user)));
                }

                if(searchParams.get(ActionLog.Fields.command) != null){
                    predicates.add(cb.equal(root.get(ActionLog.Fields.command),
                            searchParams.get(ActionLog.Fields.command)));
                }

                if(searchParams.get(ActionLog.Fields.status) != null){
                    predicates.add(cb.equal(root.get(ActionLog.Fields.status),
                            searchParams.get(ActionLog.Fields.status)));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Query(value = "select count(al) from ActionLog al " +
            "where al.command = ?1 group by al.user.id")
    List<Integer> findAllByCommandAndGroupByUserId(TelegramCommand command);

    @Query(value = "select count(al) from ActionLog al " +
            "where al.status = ?1 group by al.user.id")
    List<Integer> findAllByStatusAndGroupByUserId(ActionLogStatus status);
}
