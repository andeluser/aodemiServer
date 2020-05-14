package factory;

import card.CardAbility;
import shield.ShieldAbility;
import special.SpecialAbility;

public class AbilityFactory {

	public CardAbility getCardAbility(String cardId) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Class<?> cls = Class.forName("card." + cardId);
		CardAbility obj = (CardAbility)cls.newInstance();

		return obj;
	}

	public ShieldAbility getShieldAbility(String shieldId) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Class<?> cls = Class.forName("shield." + shieldId);
		ShieldAbility obj = (ShieldAbility)cls.newInstance();

		return obj;
	}

	public SpecialAbility getSpecialAbility(String specialId) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		Class<?> cls = Class.forName("special." + specialId);
		SpecialAbility obj = (SpecialAbility)cls.newInstance();

		return obj;
	}

}
