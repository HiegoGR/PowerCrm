package com.teste.PowerCrm.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class CrudPadraoService<E, D> {

    private final JpaRepository<E, Long> repository;
    private final Function<D, E> dtoToEntity;
    private final Function<E, D> entityToDto;

    protected CrudPadraoService(JpaRepository<E, Long> repository,
                                Function<D, E> dtoToEntity,
                                Function<E, D> entityToDto) {
        this.repository = repository;
        this.dtoToEntity = dtoToEntity;
        this.entityToDto = entityToDto;
    }

    public D salvar(D dto) {
        E entity = dtoToEntity.apply(dto);
        E salvo = repository.save(entity);
        return entityToDto.apply(salvo);
    }

    public List<D> listarTodos() {
        return repository.findAll().stream()
                .map(entityToDto)
                .toList();
    }

    public Optional<D> buscarPorId(Long id) {
        return repository.findById(id)
                .map(entityToDto);
    }

    public D atualizar(Long id, D dto) {
        E entity = dtoToEntity.apply(dto);
        entity = atualizarCampos(entity, id);
        return entityToDto.apply(repository.save(entity));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }

    protected E atualizarCampos(E entity, Long id) {
        return entity;
    }

    protected JpaRepository<E, Long> getRepository() {
        return repository;
    }

    protected Function<E, D> getEntityToDto() {
        return entityToDto;
    }

    protected Function<D, E> getDtoToEntity() {
        return dtoToEntity;
    }
}
