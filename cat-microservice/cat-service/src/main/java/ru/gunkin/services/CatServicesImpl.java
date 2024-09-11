package ru.gunkin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gunkin.abstractions.CatRepository;
import ru.gunkin.contracts.CatServices;
import ru.gunkin.dto.CatDto;
import ru.gunkin.dto.FriendDto;
import ru.gunkin.mappers.CatMapper;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Cat;
import ru.gunkin.models.Color;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CatServicesImpl implements CatServices {
    private final CatRepository catsRepository;
    private final CatMapper catMapper;

    @Override
    public void addCat(CatDto catDto) {
        var cat = catMapper.toModel(catDto);
        catsRepository.save(cat);
    }

    @Override
    public void update(CatDto catDto) {
        var catOptional = catsRepository.findById(catDto.getId());

        if (catOptional.isPresent()) {
            Cat cat = catOptional.get();
            updateCatFromDto(cat, catDto);
            catsRepository.save(cat);
        }
    }

    @Override
    @Transactional
    public void addFriend(FriendDto friendDto) {
        var cat = catsRepository.findById(friendDto.getCatId());

        var friend = catsRepository.findById(friendDto.getFriendId());

        if (cat.isEmpty() || friend.isEmpty()) {
            return;
        }

        cat.get().getFriends().add(friend.get());
        friend.get().getFriends().add(cat.get());

        catsRepository.save(cat.get());
        catsRepository.save(friend.get());
    }

    @Override
    @Transactional
    public void deleteFriend(FriendDto friendDto) {
        var cat = catsRepository.findById(friendDto.getCatId());
        var friend = catsRepository.findById(friendDto.getFriendId());

        if (cat.isEmpty() || friend.isEmpty()) {
            return;
        }

        cat.get().getFriends().remove(friend.get());
        friend.get().getFriends().remove(cat.get());

        catsRepository.save(cat.get());
        catsRepository.save(friend.get());
    }

    @Override
    @Transactional
    public void deleteCat(long id) {
        var cat = catsRepository.findById(id);

        if (cat.isEmpty()) {
            return;
        }

        for (Cat friend : cat.get().getFriends()) {
            friend.getFriends().remove(cat.get());
            catsRepository.save(friend);
        }

        catsRepository.delete(cat.get());
    }

    @Override
    public Optional<CatDto> findCat(long id) {
        return catsRepository.findById(id).map(catMapper::toDto);
    }

    @Override
    public List<CatDto> getFriends(long id) {
        var cat = catsRepository.findById(id);

        if (cat.isEmpty()) {
            return List.of();
        }

        return catMapper.toDto(cat.get().getFriends());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatDto> findAllCats(String name, Breed breed, Color color, Long ownerId) {
        Specification<Cat> specification = createSpecification(name, breed, color, ownerId);

        return catsRepository.findAll(specification).stream()
                .map(catMapper::toDto)
                .collect(Collectors.toList());
    }

    private void updateCatFromDto(Cat cat, CatDto catDto) {
        if (catDto.getName() != null) {
            cat.setName(catDto.getName());
        }
        if (catDto.getBirthDate() != null) {
            cat.setBirthDate(catDto.getBirthDate());
        }
        if (catDto.getBreed() != null) {
            cat.setBreed(catDto.getBreed());
        }
        if (catDto.getColor() != null) {
            cat.setColor(catDto.getColor());
        }
        if (catDto.getOwnerId() != null) {
            cat.setOwnerId(catDto.getOwnerId());
        }
    }

    private Specification<Cat> createSpecification(String name, Breed breed, Color color, Long ownerId) {
        Specification<Cat> specification = Specification.where(null);

        if (name != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name));
        }

        if (breed != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("breed"), breed));
        }

        if (color != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("color"), color));
        }

        if (ownerId != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("ownerId"), ownerId));
        }

        return specification;
    }
}
