package ru.gunkin.services;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.gunkin.abstractions.OwnerRepository;
import ru.gunkin.contracts.OwnerServices;
import ru.gunkin.dto.AddOwnerDto;
import ru.gunkin.dto.OwnerDto;
import ru.gunkin.mappers.OwnerMapper;
import ru.gunkin.models.Owner;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnerServicesImpl implements OwnerServices {
    private final OwnerRepository ownersRepository;
    private final OwnerMapper ownerMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public void addOwner(AddOwnerDto addDto) {
        var result = ownersRepository.save(ownerMapper.toModel(addDto.getOwnerDto()));
        kafkaTemplate.send("setOwnerToUser",
                AddOwnerDto.builder()
                        .username(addDto.getUsername())
                        .ownerDto(ownerMapper.toDto(result))
                        .build());
    }

    @Override
    @Transactional
    public void updateOwner(OwnerDto ownerDto) {
        var ownerOptional = ownersRepository.findById(ownerDto.getId());

        if (ownerOptional.isPresent()) {
            Owner owner = ownerOptional.get();
            updateOwnerFromDto(owner, ownerDto);
            ownersRepository.save(owner);
        }
    }

    @Override
    public void deleteOwner(long id) {
        ownersRepository.deleteById(id);
    }

    @Override
    public Optional<OwnerDto> findOwner(long id) {
        return ownersRepository.findById(id).map(ownerMapper::toDto);
    }

    @Override
    public List<OwnerDto> findAllOwners() {
        return ownerMapper.toDto(ownersRepository.findAll());
    }

    private void updateOwnerFromDto(Owner owner, OwnerDto ownerDto) {
        if (ownerDto.getName() != null) {
            owner.setName(ownerDto.getName());
        }
        if (ownerDto.getBirthDate() != null) {
            owner.setBirthDate(ownerDto.getBirthDate());
        }
    }
}
