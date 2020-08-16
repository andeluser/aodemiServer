package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//支援部隊
public class m12 implements CardAbility {

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

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		if (baseDto.getSpecial_stock() == 0 &&  baseDto.getSpecial_gage() <= 4) {
			return ret;
		}

		//奥義ゲージを５減らす
		int gage = baseDto.getSpecial_gage() - 5;
		int stock = baseDto.getSpecial_stock();

		boolean gageFlg = false;
		if (gage < 0) {

			if (stock > 0) {
				gage = 15;
				stock = stock - 1;
				baseDto.setSpecial_stock(stock);
				gageFlg = true;
			} else {
				gage = 0;
			}

		}

		baseDto.setSpecial_gage(gage);
		baseDao.update(baseDto);

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		detailMap.put("playerId", playerId);
		detailMap.put("gage", baseDto.getSpecial_gage());

		if (gageFlg) {
			detailMap.put("stock", baseDto.getSpecial_stock());
		}

		retList.add(detailMap);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("special", retList);

		ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, playerId);
		ArrayList<Object> retList2 = new ArrayList<Object>();

		for (int i = 0; i < fieldList.size(); i++) {
			BattleFieldDTO dto = fieldList.get(i);

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0 && "黒".equals(dto.getColor())) {
				//戻り値の作成
				HashMap<String, Object> detailMap2 = new HashMap<String, Object>();

				dto.setTurn_atk(dto.getTurn_atk() + 10);
				fieldDao.update(dto);

				detailMap2.put("playerId", playerId);
				detailMap2.put("fieldNumber", dto.getField_no());
				detailMap2.put("tupATK", dto.getTurn_atk());
				retList2.add(detailMap2);

			}

		}

		if (retList2.size() != 0) {
			updateMap.put("field", retList2);
			updateList.add(updateMap);
		}

		//戻り値設定
		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
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
