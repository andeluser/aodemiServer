package dao;

import java.util.ArrayList;

import dto.BattleShieldDTO;

public interface BattleShieldDAO {

	BattleShieldDTO getAllValue(String battleID, String playerId, int shieldNumber) throws Exception;
	ArrayList<BattleShieldDTO> getAllList(String battleID, String playerId) throws Exception;
	int insert(BattleShieldDTO dto) throws Exception;
	int deleteBattle(BattleShieldDTO dto) throws Exception;
	int update(BattleShieldDTO dto) throws Exception;
}
