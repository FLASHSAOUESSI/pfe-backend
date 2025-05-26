package com.ins.insstatistique.service;

import java.util.List;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.EntrepriseStatus;
import com.ins.insstatistique.entity.Governorate;
import com.ins.insstatistique.entity.Role;
import com.ins.insstatistique.entity.User;
import com.ins.insstatistique.repository.EntrepriseRepository;
import com.ins.insstatistique.repository.GovernorateRepository;
import com.ins.insstatistique.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MockDataService {

    private final EntrepriseRepository entrepriseRepository;
    private final UserRepository userRepository;
    private final GovernorateRepository governorateRepository;
    private final PasswordEncoder passwordEncoder;
    
    private final Random random = new Random();
    
    private static final String[] COMPANY_PREFIXES = {
        "Tunisie", "Med", "North", "Atlas", "Sahara", "Carthage", "Africa", 
        "Djerba", "Neo", "Tech", "DataTech", "Arab", "Maghreb", "Euro",
        "Global", "Inter", "Mega", "Smart", "Future", "Innov", "Digital"
    };
    
    private static final String[] COMPANY_SUFFIXES = {
        "Tech", "Soft", "Systems", "Solutions", "Corp", "Group", "Industries",
        "Services", "Media", "Data", "Consulting", "Partners", "Networks",
        "Logistics", "Trading", "Exports", "Commerce", "Telecom", "Cloud", "AI"
    };
    
    private static final String[] DOMAINS = {
        "com", "tn", "org", "net", "io", "tech", "digital", "group"
    };
    
    private static final String[] STREET_PREFIXES = {
        "Rue", "Avenue", "Boulevard", "Place", "Impasse"
    };
    
    private static final String[] STREET_NAMES = {
        "de Carthage", "Habib Bourguiba", "de Paris", "Mohamed V", "de la Liberté", 
        "Ibn Khaldoun", "14 Janvier", "de Marseille", "de Londres", "d'Afrique", 
        "du Lac", "de la République", "des Oliviers", "de la Méditerranée"
    };

    @Transactional
    public void generateMockEnterprises(int count) {
        // Make sure there are governorates in the database
        List<Governorate> governorates = governorateRepository.findAll();
        if (governorates.isEmpty()) {
            throw new RuntimeException("Please initialize governorates first by calling the /api/governorates/initialize endpoint");
        }
        
        for (int i = 0; i < count; i++) {
            // Create an enterprise with random data
            Entreprise enterprise = createRandomEnterprise(governorates);
            entrepriseRepository.save(enterprise);
            
            // Create a user for each enterprise
            User user = createRandomUser(enterprise);
            userRepository.save(user);
        }
    }
    
    private Entreprise createRandomEnterprise(List<Governorate> governorates) {
        String name = generateRandomCompanyName();
        String email = generateRandomEmail(name);
        Governorate governorate = getRandomItem(governorates);
        
        return Entreprise.builder()
                .name(name)
                .email(email)
                .address(generateRandomAddress())
                .fax(generateRandomPhoneNumber())
                .governorate(governorate)
                .status(getRandomStatus())
                .build();
    }
    
    private User createRandomUser(Entreprise enterprise) {
        String name = generateRandomPersonName();
        String email = generateRandomEmail(name);
        
        return User.builder()
                .nom(name)
                .email(email)
                .phone(generateRandomPhoneNumber())
                .password(passwordEncoder.encode("password123"))
                .entreprise(enterprise)
                .role(Role.RESPONSABLE)
                .enabled(true)
                .build();
    }
    
    private String generateRandomCompanyName() {
        String prefix = getRandomItem(COMPANY_PREFIXES);
        String suffix = getRandomItem(COMPANY_SUFFIXES);
        return prefix + " " + suffix;
    }
    
    private String generateRandomPersonName() {
        String[] firstNames = {"Mohamed", "Ali", "Ahmed", "Youssef", "Amine", "Sami", "Nabil", 
                              "Fatma", "Leila", "Sana", "Amira", "Rania", "Salma", "Yasmine"};
        String[] lastNames = {"Ben Ali", "Trabelsi", "Mansour", "Abidi", "Khalil", "Mejri", 
                             "Jabri", "Zouhair", "Soussi", "Gharbi", "Ferchichi", "Haddad"};
                             
        return getRandomItem(firstNames) + " " + getRandomItem(lastNames);
    }
    
    private String generateRandomEmail(String name) {
        String domain = getRandomItem(DOMAINS);
        String normalizedName = name.toLowerCase()
                .replace(" ", ".")
                .replaceAll("[^a-z.]", "");
        return normalizedName + "@company." + domain;
    }
    
    private String generateRandomAddress() {
        String streetPrefix = getRandomItem(STREET_PREFIXES);
        String streetName = getRandomItem(STREET_NAMES);
        int number = random.nextInt(1, 300);
        return number + " " + streetPrefix + " " + streetName;
    }
    
    private String generateRandomPhoneNumber() {
        String[] prefixes = {"71", "70", "72", "73", "74", "75", "76", "77", "78", "79"};
        String prefix = getRandomItem(prefixes);
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    private EntrepriseStatus getRandomStatus() {
        EntrepriseStatus[] statuses = EntrepriseStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
    
    private <T> T getRandomItem(T[] array) {
        return array[random.nextInt(array.length)];
    }
    
    private <T> T getRandomItem(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
