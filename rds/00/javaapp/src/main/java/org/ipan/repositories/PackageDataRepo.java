package org.ipan.repositories;

import java.util.Optional;
import java.util.UUID;

import org.ipan.model.Package;
import org.ipan.model.Person;
import org.springframework.data.repository.CrudRepository;


public interface PackageDataRepo extends CrudRepository<Package, UUID> {
}
