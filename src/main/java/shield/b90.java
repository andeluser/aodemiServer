package shield;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//奇跡の顕現
public class b90 implements ShieldAbility {

	@Override
	public HashMap<String, Object> shieldSkill(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleDeckDAO deckDao = factory.createDeckDAO();

		//相手の対象計算
		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		ArrayList<Object> retList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO dto = fieldDtoList.get(i);

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 1) {

				BattleDeckDTO deckDto = deckDao.getAllValue(battleID, playerId, dto.getDeck_no());

				//対象のユニットを復活させる
				dto.setClose(0);
				dto.setAction(0);
				dto.setClose_number(0);
				dto.setOpen_close_number(0);

				//復活に伴う初期化処理
				if (deckDto.getClose_skill() == 1) {
					dto.setClose_skill(0);
				} else {
					dto.setClose_skill(1);
				}
				dto.setAction(0);
				dto.setPermanent_hp(0);
				dto.setTurn_hp(0);
				dto.setCur_hp(dto.getBase_hp());
				dto.setPermanent_level(0);
				dto.setTurn_level(0);
				dto.setPermanent_atk(0);
				dto.setTurn_atk(0);
				dto.setPermanent_def(0);
				dto.setTurn_def(0);
				dto.setPermanent_speed(0);
				dto.setTurn_speed(0);
				dto.setPermanent_range(0);
				dto.setTurn_range(0);

				fieldDao.update(dto);

				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", playerId);
				detailMap.put("fieldNumber", i);
				detailMap.put("remove", "revive");
				retList.add(detailMap);
			}
		}

		if (retList.size() == 0) {
			//対象が一人も居ない場合は処理終了
			return new HashMap<String, Object>();
		}

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> shieldSkillSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
