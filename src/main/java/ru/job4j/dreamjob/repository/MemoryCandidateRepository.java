package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    public MemoryCandidateRepository() {
        save(new Candidate(0, "Petrov Ivan Sergeevich", "1.5 years experience",
                LocalDateTime.now(), 3, 0));
        save(new Candidate(0, "Ivanov Sergey Aleksandrovich", "2.5 years experience",
                LocalDateTime.now(), 2, 0));
        save(new Candidate(0, "Lapshin Oleg Petrovich", "5 years experience",
                LocalDateTime.now(), 2, 0));
        save(new Candidate(0, "Egorov Igor Alekseevich", "11 years experience",
                LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Leonova Marina Leonidovna", "0.5 years experience",
                LocalDateTime.now(), 3, 0));
        save(new Candidate(0, "Popov Maksim Mihailovich", "0 years experience",
                LocalDateTime.now(), 1, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.getAndIncrement());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldCandidate) -> new Candidate(oldCandidate.getId(), candidate.getName(),
                candidate.getDescription(), candidate.getCreationDate(), candidate.getCityId(), candidate.getFileId())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }

}