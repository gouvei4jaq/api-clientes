package com.api_clientes.repository;

import com.api_clientes.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository <ClientEntity,Long> {

}
