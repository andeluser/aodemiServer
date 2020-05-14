package dao;

import java.util.ArrayList;

import dto.BattleFieldDTO;

public interface BattleFieldDAO {
	BattleFieldDTO getAllValue(String battleID, String playerId, int fieldNumber) throws Exception;
	ArrayList<BattleFieldDTO> getAllList(String battleID, String playerId) throws Exception;
	int insert(BattleFieldDTO dto) throws Exception;
	int deleteBattle(BattleFieldDTO dto) throws Exception;
	int update(BattleFieldDTO dto) throws Exception;
	void update(ArrayList<BattleFieldDTO> list) throws Exception;
	BattleFieldDTO getNextClose(String battleID) throws Exception;
	void checkOpenClose(String battleID) throws Exception;
	BattleFieldDTO getNextCloseSkill(String battleID) throws Exception;
	void setClose(String battleID, String playerId, int fieldNumber, BattleFieldDTO fieldDto) throws Exception;
}
