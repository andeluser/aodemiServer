package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//炎鬼『エンキ』
public class m40 implements CardAbility {

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
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		if (fieldDto.getCur_hp() != 1) {
			return ret;
		}

		ArrayList<Object> retList = new ArrayList<Object>();

		//HPを赤属性×１０回復
		int maxHp = fieldDto.getPermanent_hp() + fieldDto.getTurn_hp() + fieldDto.getBase_hp();

		int hp = fieldDto.getCur_hp() + (baseDto.getRed() * 10);

		if (hp > maxHp) {
			hp = maxHp;
		}

		fieldDto.setCur_hp(hp);
		fieldDao.update(fieldDto);

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
		detailMap.put("hp", hp);
		retList.add(detailMap);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", retList);
		updateList.add(updateMap);

		//戻り値設定
		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//ATK+自分のHP
		fieldDto.setPermanent_atk(fieldDto.getPermanent_atk() + fieldDto.getCur_hp());
		fieldDto.setCur_hp(1);
		fieldDao.update(fieldDto);

		//戻り値を設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();

		updateMap.put("fieldNumber", fieldNumber);
		updateMap.put("upATK", fieldDto.getPermanent_atk());
		updateMap.put("hp", 1);
		updateMap.put("playerId", playerId);
		ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();
		updateList.add(updateMap);

		HashMap<String, Object> orderMap = new HashMap<String, Object>();
		ArrayList<Object> orderList = new ArrayList<Object>();

		orderMap.put("field", updateList);
		orderList.add(orderMap);

		ret.put("updateInfo", orderList);

		return ret;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action2(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect2(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> close(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
