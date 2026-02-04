package com.idk.essence.players.providers;

import com.idk.essence.magics.DomainAccordance;
import com.idk.essence.magics.MagicDomain;
import com.idk.essence.magics.MagicManager;
import com.idk.essence.magics.domains.ElementDomain;
import com.idk.essence.magics.domains.FeatureDomain;
import com.idk.essence.magics.domains.IntensityDomain;
import com.idk.essence.magics.domains.OriginDomain;
import com.idk.essence.players.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public interface MagicDataProvider extends DataProvider {

    List<String> getGottenAptitudes();
    List<String> getLearnedDomains();
    List<String> getLearnedSignets();

    default List<String> getInitialAptitudes() {
        List<MagicDomain> aptitudes = new ArrayList<>();
        aptitudes.addAll(aptitudeCategory(DomainAccordance.ELEMENT, ElementDomain.getLeastAptitude()));
        aptitudes.addAll(aptitudeCategory(DomainAccordance.FEATURE, FeatureDomain.getLeastAptitude()));
        aptitudes.addAll(aptitudeChain(DomainAccordance.INTENSITY, IntensityDomain.getLeastAptitude()));
        aptitudes.addAll(aptitudeCategory(DomainAccordance.ORIGIN, OriginDomain.getLeastAptitude()));
        // Use ArrayList to make the list mutable
        return aptitudes.stream().map(MagicDomain::getInternalName).collect(Collectors.toCollection(ArrayList::new));
    }

    default List<MagicDomain> aptitudeCategory(DomainAccordance accordance, int least) {
        List<MagicDomain> candidates = MagicManager.getDomainsByAccordance(accordance);
        List<MagicDomain> awakened = new ArrayList<>();

        // Get aptitude by chance
        for(MagicDomain domain : candidates) {
            if(ThreadLocalRandom.current().nextDouble() < domain.getAptitudeChance()) {
                awakened.add(domain);
            }
        }

        // Supplement to the minimum value
        while(awakened.size() < least && !candidates.isEmpty()) {
            MagicDomain fallback = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
            if(!awakened.contains(fallback)) {
                awakened.add(fallback);
            }
        }

        return awakened;
    }

    /**
     * For example, if F: 1, E: 0.95, D: 0.9, then D is 1 * 0.95 * 0.9
     */
    default List<MagicDomain> aptitudeChain(DomainAccordance accordance, int least) {
        List<MagicDomain> candidates = MagicManager.getDomainsByAccordance(accordance);
        List<MagicDomain> awakened = new ArrayList<>();

        for(MagicDomain domain : candidates) {
            if(awakened.size() < least || ThreadLocalRandom.current().nextDouble() < domain.getAptitudeChance()) {
                awakened.add(domain);
            } else {
                break;
            }
        }
        return awakened;
    }
}
