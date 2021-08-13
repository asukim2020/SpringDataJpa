package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // @NamedQuery로 조회
//    @Query(name = "Member.findByUsername") 사용 안함
    List<Member> findByUsername(@Param("username") String username);

    // 쿼리로 조회
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // 인자 값 조회
    @Query("select m.username from Member m")
    List<String> findUserNameList();

    // Dto 로 조회
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 파라미터 바인딩 - in 절
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    // 유연한 반환타입
    List<Member> findListByUsername(String username);
    Member findMEmberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);

    // 페이징
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m") // 카운터 쿼리 최적화
    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);
}
