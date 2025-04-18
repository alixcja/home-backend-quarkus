package de.explore.grabby.lunch.repository;

import de.explore.grabby.lunch.model.Address;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<Address> {
}
