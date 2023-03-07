package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    public MemoryCandidateRepository() {
        save(new Candidate(0, "Ivan Petrov", "1.5 years experience",
                LocalDateTime.now()));
        save(new Candidate(0, "Sergey Ivanov", "2.5 years experience",
                LocalDateTime.now()));
        save(new Candidate(0, "Oleg Lapshin", "5 years experience",
                LocalDateTime.now()));
        save(new Candidate(0, "Igor Egorov", "11 years experience",
                LocalDateTime.now()));
        save(new Candidate(0, "Marina Leonova", "0.5 years experience",
                LocalDateTime.now()));
        save(new Candidate(0, "Maksim Popov", "0 years experience",
                LocalDateTime.now()));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
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
                candidate.getDescription(), candidate.getCreationDate())) != null;
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