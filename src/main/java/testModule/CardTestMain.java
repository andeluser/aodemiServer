package testModule;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import card.CardAbility;
import factory.AbilityFactory;
import util.AodemiLogger;
import util.StringUtil;

public class CardTestMain {

	public static void main(String[] args) {

		String battleID = "T0001";
		//プレイヤー１
		String playerId1 = "A";
		//対象の位置
		int fieldNumber = 1;

		try {

			AodemiLogger logger = AodemiLogger.getInstance(battleID, "", true);

			StringUtil stringUtil = new StringUtil();

			String targetString = "[{\"targetList\":[{\"playerId\":\"A\",\"list\":[0]}]}]";

			ArrayList target = stringUtil.getJsonArray(targetString);

			AbilityFactory ability = new AbilityFactory();

//			HashMap<String, Object> result = new HashMap<String, Object>();
//			HashMap<String, Object> result2 = new HashMap<String, Object>();
//
//			ExpansionUtil util = new ExpansionUtil();
//			boolean cheack = util.returnCheack("b4");
//			System.out.println(cheack);

			HashMap<String, Object> result = new HashMap();
			HashMap<String, Object> result2 = new HashMap();

			/*
			 *
			 * 各種カードのテスト用
			 *
			 */

			CardAbility cardAbility = ability.getCardAbility("b20");

//			result = cardAbility.open(battleID, playerId1);
//			result2 = cardAbility.openSelect(battleID, playerId1, target);
//			result = cardAbility.start(battleID, playerId1, fieldNumber);
//			result2 = cardAbility.startSelect(battleID, playerId1, target, fieldNumber);
//			result = cardAbility.auto(battleID, playerId1, fieldNumber);
//			result2 = cardAbility.autoSelect(battleID, playerId1, target, fieldNumber);
			result = cardAbility.action1(battleID, playerId1, fieldNumber);
			result2 = cardAbility.actionSelect1(battleID, playerId1, target, fieldNumber);
//			result = cardAbility.action2(battleID, playerId1, fieldNumber);
//			result2 = cardAbility.actionSelect2(battleID, playerId1, target, fieldNumber);
//			result = cardAbility.close(battleID, playerId1, fieldNumber);
//			result2 = cardAbility.closeSelect(battleID, playerId1, target, fieldNumber);

			/*
			 *
			 * シールドのテスト用
			 *
			 */

//			ShieldAbility shieldAbility = ability.getShieldAbility("b85");
//			result = shieldAbility.shieldSkill(battleID, playerId1);
//			result2 = shieldAbility.shieldSkillSelect(battleID, playerId1);

			/*
			 *
			 * 奥義のテスト用
			 *
			 */

//			SpecialAbility specialAbility = ability.getSpecialAbility("o1");
//			result = specialAbility.specialSkill(battleID, playerId1);
//			result2 = specialAbility.specialSkillSelect(battleID, playerId1);


			System.out.println("result1:" + stringUtil.getJsonStr(result));
			System.out.println("result2:" + stringUtil.getJsonStr(result2));


		} catch (Exception e) {

			 try {

	            	StringWriter sw = new StringWriter();
	            	PrintWriter pw = new PrintWriter(sw);
	            	e.printStackTrace(pw);
	            	pw.flush();

	            	System.out.println(sw.toString());

			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}

	}

}
