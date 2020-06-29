package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dto.BattleBaseDTO;
import factory.DaoFactory;

//一斉突撃
public class m34 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		//自分のストックを１増やす
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);
		int stock = baseDto.getSpecial_stock() + 2;

		if (stock >= 5) {
			stock = 5;
		}
		baseDto.setSpecial_stock(stock);

		//奥義発動状態にする
		baseDto.setSpecial_use_bye_skill(1);

		baseDao.update(baseDto);

		HashMap<String, Object> detailMapSpecial = new HashMap<String, Object>();
		detailMapSpecial.put("playerId", playerId);
		detailMapSpecial.put("stock", baseDto.getSpecial_stock());

		ArrayList<Object> listSpecial = new ArrayList<Object>();
		listSpecial.add(detailMapSpecial);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("special", listSpecial);

		ArrayList<Object> updateList = new ArrayList<Object>();
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList());

		return ret;
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
		// TODO 自動生成されたメソッド・スタブ
		return null;
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
