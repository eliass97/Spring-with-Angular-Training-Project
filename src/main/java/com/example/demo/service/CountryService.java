package com.example.demo.service;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.DemoException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.model.Country;
import com.example.demo.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> findAll() {
        LOGGER.info("CountryService -> GET -> Searched for all");
        return countryRepository.findAll();
    }

    public Country findById(int id) throws DemoException {
        Optional<Country> result = countryRepository.findById(id);
        LOGGER.info("CountryService -> GET -> findById -> Searched for id = {}", id);
        if (result.isEmpty()) {
            LOGGER.error("CountryService -> GET -> findById -> NotFoundException -> id = {}", id);
            throw new NotFoundException("Country not found");
        }
        return result.get();
    }

    public Page<Country> findCountriesByPage(Pageable pageableRequest) {
        Page<Country> result = countryRepository.findAll(pageableRequest);
        LOGGER.info("Searched for page = {} and size = {}", pageableRequest.getPageNumber(), pageableRequest.getPageSize());
        return result;
    }

    public Country create(Country newCountry) throws DemoException {
        if (isoExistsInDatabase(newCountry.getIso())) {
            LOGGER.error("CountryService -> POST -> create -> BadRequestException -> Provided iso = {} already exists in the database", newCountry.getIso());
            throw new BadRequestException("Provided iso already exists in the database");
        }
        Country createdCountry = countryRepository.save(newCountry);
        LOGGER.info("CountryService -> POST -> create -> Created {}", newCountry.toString());
        return createdCountry;
    }

    public Country update(int pathId, Country updatedCountry) throws DemoException {
        updateChecks(pathId, updatedCountry);
        Country countryToBeUpdated = findById(pathId);
        countryToBeUpdated.check(updatedCountry.getLastUpdateDate());
        Country result = updateAndSaveInDatabase(updatedCountry, countryToBeUpdated);
        LOGGER.info("CountryService -> PUT -> update -> Updated {}", result);
        return result;
    }

    private void updateChecks(int pathId, Country updatedCountry) throws DemoException {
        if (updatedCountry.getId() != pathId && updatedCountry.getId() != 0) {
            LOGGER.error("CountryService -> PUT -> updateChecks -> BadRequestException -> path_id = {} and body_id = {} do not match", pathId, updatedCountry.getId());
            throw new BadRequestException("Path ID variable does not match with body ID");
        }
        if (isoExistsInDatabase(updatedCountry.getIso()) && getCountryByIso(updatedCountry.getIso()).getId() != pathId) {
            LOGGER.error("CountryService -> PUT -> updateChecks -> BadRequestException -> Provided iso = {} already exists in the database", updatedCountry.getIso());
            throw new BadRequestException("Provided iso already exists in the database");
        }
    }

    private Country updateAndSaveInDatabase(Country updatedCountry, Country countryToBeUpdated) {
        countryToBeUpdated.setIso(updatedCountry.getIso());
        countryToBeUpdated.setDescription(updatedCountry.getDescription());
        countryToBeUpdated.setPrefix(updatedCountry.getPrefix());
        return countryRepository.save(countryToBeUpdated);
    }

    public void delete(int id) throws DemoException {
        findById(id);
        countryRepository.deleteById(id);
        LOGGER.info("CountryService -> DELETE -> delete -> Deleted country with id = {}", id);
    }

    private boolean isoExistsInDatabase(String iso) {
        try {
            getCountryByIso(iso);
        } catch (DemoException e) {
            return false;
        }
        return true;
    }

    public Country getCountryByIso(String iso) throws DemoException {
        Optional<Country> result = countryRepository.findByIso(iso);
        if (result.isEmpty()) {
            throw new NotFoundException("Country not found for iso = " + iso);
        }
        return result.get();
    }

    public long getNumberOfCountries() {
        return countryRepository.count();
    }
}
