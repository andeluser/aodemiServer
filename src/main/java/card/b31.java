package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.StringUtil;

//スケルトンナイト
public class b31 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> openSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> start(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action2(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect2(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> close(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleDeckDAO DeckDao = factory.createDeckDAO();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		ArrayList<BattleDeckDTO> deckList = DeckDao.getAllList(battleID, playerId);
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);
		StringUtil stringUtil = new StringUtil();

		//自分のカードIDを取得
		String cardId = fieldDto.getCard_id();

		ArrayList<Object> orderList = new ArrayList<Object>();

		for (int i = 0; i < deckList.size(); i++) {
			BattleDeckDTO dto = deckList.get(i);

			if (dto.getCard_id() != null && dto.getCard_out() == 0 && cardId.equals(dto.getCard_id())) {
				//デッキにカードが存在する場合は復活する。

				HashMap<String, Object> updateMap = new HashMap<String, Object>();
				int maxHp = fieldDto.getPermanent_hp() + fieldDto.getTurn_hp() + fieldDto.getBase_hp();
				fieldDto.setCur_hp(maxHp);
				fieldDao.update(fieldDto);

				updateMap.put("fieldNumber", fieldNumber);
				updateMap.put("hp", maxHp);
				updateMap.put("playerId", playerId);
				ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();
				updateList.add(updateMap);

				HashMap<String, Object> orderMap = new HashMap<String, Object>();

				orderMap.put("field", updateList);

				//デッキのカードは墓地に
				dto.setCard_out(1);
				DeckDao.update(dto);
				baseDto.setCemetery(stringUtil.addCardNumberForCamma(baseDto.getCemetery(), dto.getDeck_no()));
				baseDao.update(baseDto);

				HashMap<String, Object> updateMap2 = new HashMap<String, Object>();
				ArrayList<HashMap<String, Object>> updateList2 = new ArrayList<HashMap<String, Object>>();

				updateMap2.put("playerId", fieldNumber);
				updateMap2.put("deckNumber", dto.getDeck_no());
				updateList2.add(updateMap2);
				orderMap.put("deckCemetery", updateList2);

				orderList.add(orderMap);

			}
		}

		if (orderList.size() != 0) {
			ret.put("updateInfo", orderList);
		}

		return ret;
	}

	@Override
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
