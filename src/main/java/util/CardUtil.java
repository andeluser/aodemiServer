package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cardDataObject.Card;
import cardDataObject.Deck;
import cardDataObject.Shield;
import cardDataObject.Special;

public class CardUtil {

	private Workbook bfBook;
	private Sheet cardSheet;
	private Sheet shieldSheet;
	private Sheet specialSheet;
	private HashMap<String, Integer> cardColumIndex;
	private HashMap<String, Integer> shieldColumIndex;
	private HashMap<String, Integer> specialColumIndex;

	public CardUtil(String inputFilePath) throws IOException {
		FileInputStream bfStream =new FileInputStream(inputFilePath + "cardMaster.xlsx");
		this.bfBook =new XSSFWorkbook(bfStream);
		this.cardSheet = bfBook.getSheet("CardMaster");
		this.shieldSheet = bfBook.getSheet("ShieldMaster");
		this.specialSheet = bfBook.getSheet("SpecialMaster");

		cardColumIndex = getCardColumIndex(this.cardSheet);
		shieldColumIndex = getCardColumIndex(this.shieldSheet);
		specialColumIndex = getCardColumIndex(this.specialSheet);

        bfStream.close();
	}

	//指定したカード情報の取得
	public Card getCard(String cardId) {
		Card ret = new Card();
		String id = "";

		if ("".equals(cardId)) {
			return ret;
		}

		int index = 0;

		for(Row row : this.cardSheet) {
				//データが
				id = getStringCellValue(row.getCell(0));

				if ("".equals(id)) {
					break;
				} else if(cardId.equals(id)) {
					//カードIDが一致したらその情報を設定
					ret.setId(getStringCellValue(row.getCell(this.cardColumIndex.get("id"))));
					ret.setType(getStringCellValue(row.getCell(this.cardColumIndex.get("type"))));
					ret.setColor(getStringCellValue(row.getCell(this.cardColumIndex.get("color"))));
					ret.setType1(getStringCellValue(row.getCell(this.cardColumIndex.get("type1"))));
					ret.setType2(getStringCellValue(row.getCell(this.cardColumIndex.get("type2"))));
					ret.setLevel(getStringCellValue(row.getCell(this.cardColumIndex.get("level"))));
					ret.setFrm(getStringCellValue(row.getCell(this.cardColumIndex.get("frm"))));
					ret.setHp(getStringCellValue(row.getCell(this.cardColumIndex.get("hp"))));
					ret.setAtk(getStringCellValue(row.getCell(this.cardColumIndex.get("atk"))));
					ret.setDfe(getStringCellValue(row.getCell(this.cardColumIndex.get("dfe"))));
					ret.setAgi(getStringCellValue(row.getCell(this.cardColumIndex.get("agi"))));
					ret.setRng(getStringCellValue(row.getCell(this.cardColumIndex.get("rng"))));

					ret.setOpenSkillName(getStringCellValue(row.getCell(this.cardColumIndex.get("openSkillName"))));
					ret.setOpenSkillSP(getStringCellValue(row.getCell(this.cardColumIndex.get("openSkillSP"))));
					ret.setOpenSkillDetail(getStringCellValue(row.getCell(this.cardColumIndex.get("openSkillDetail"))));
					ret.setStartSkillName(getStringCellValue(row.getCell(this.cardColumIndex.get("startSkillName"))));
					ret.setStartSkillSP(getStringCellValue(row.getCell(this.cardColumIndex.get("startSkillSP"))));
					ret.setStartSkillDetail(getStringCellValue(row.getCell(this.cardColumIndex.get("startSkillDetail"))));
					ret.setAutoSkillName(getStringCellValue(row.getCell(this.cardColumIndex.get("autoSkillName"))));
					ret.setAutoSkillSP(getStringCellValue(row.getCell(this.cardColumIndex.get("autoSkillSP"))));
					ret.setAutoSkillDetail(getStringCellValue(row.getCell(this.cardColumIndex.get("autoSkillDetail"))));
					ret.setActionSkillName1(getStringCellValue(row.getCell(this.cardColumIndex.get("actionSkillName1"))));
					ret.setActionSkillSP1(getStringCellValue(row.getCell(this.cardColumIndex.get("actionSkillSP1"))));
					ret.setActionSkillDetail1(getStringCellValue(row.getCell(this.cardColumIndex.get("actionSkillDetail1"))));
					ret.setActionSkillName2(getStringCellValue(row.getCell(this.cardColumIndex.get("actionSkillName2"))));
					ret.setActionSkillSP2(getStringCellValue(row.getCell(this.cardColumIndex.get("actionSkillSP2"))));
					ret.setActionSkillDetail2(getStringCellValue(row.getCell(this.cardColumIndex.get("actionSkillDetail2"))));
					ret.setCloseSkillName(getStringCellValue(row.getCell(this.cardColumIndex.get("closeSkillName"))));
					ret.setCloseSkillSP(getStringCellValue(row.getCell(this.cardColumIndex.get("closeSkillSP"))));
					ret.setCloseSkillDetail(getStringCellValue(row.getCell(this.cardColumIndex.get("closeSkillDetail"))));
					ret.setTextSkillName(getStringCellValue(row.getCell(this.cardColumIndex.get("textSkillName"))));
					ret.setTextSkillDetail(getStringCellValue(row.getCell(this.cardColumIndex.get("textSkillDetail"))));

					index++;

					break;
				}
		}

		return ret;
	}

	//指定したシールド情報の取得
	public Shield getShield(String shieldId) {
		Shield ret = new Shield();

		String id = "";

		if ("".equals(shieldId)) {
			return ret;
		}

		for(Row row : this.shieldSheet) {

			id = row.getCell(0).getStringCellValue();

			if ("".equals(id)) {
				break;
			} else if(shieldId.equals(id)) {
				//カードIDが一致したらその情報を設定
				ret.setId(getStringCellValue(row.getCell(this.shieldColumIndex.get("id"))));
				ret.setLife(getStringCellValue(row.getCell(this.shieldColumIndex.get("life"))));
				ret.setSkillName(getStringCellValue(row.getCell(this.shieldColumIndex.get("skillName"))));
				ret.setSkillSP(getStringCellValue(row.getCell(this.shieldColumIndex.get("skillSP"))));
				if ("TRUE".equals(row.getCell(this.shieldColumIndex.get("skillSP")).toString())) {
					ret.setSkillSelect("1");
				} else {
					ret.setSkillSelect("0");
				}
				ret.setSkillSP(getStringCellValue(row.getCell(this.shieldColumIndex.get("skillSelect"))));
				ret.setSkillDetail(getStringCellValue(row.getCell(this.shieldColumIndex.get("skillDetail"))));

				break;
			}
		}

		return ret;
	}

	//指定した特殊情報の取得
	public Special getSpecial(String specialId) {
		Special ret = new Special();

		if ("".equals(specialId)) {
			return ret;
		}

		String id = this.specialSheet.getRow(1).getCell(0).getStringCellValue();

		for(Row row : this.specialSheet) {

			id = row.getCell(0).getStringCellValue();

			if ("".equals(id)) {
				break;
			} else if(specialId.equals(id)) {
				//スペシャルIDが一致したらその情報を設定
				ret.setId(getStringCellValue(row.getCell(this.specialColumIndex.get("id"))));
				ret.setColor(getStringCellValue(row.getCell(this.specialColumIndex.get("color"))));
				ret.setStock(getStringCellValue(row.getCell(this.specialColumIndex.get("stock"))));
				break;
			}
		}

		return ret;
	}

	//デッキ番号を指定してデッキ情報を取得する
	public Deck getDeck(String userId, String deckNumber, String path) throws IOException {

		Deck deck = new Deck();

		FileInputStream bfStream =new FileInputStream(path + "deckInfo" + deckNumber + ".xlsx");
		Workbook wb =new XSSFWorkbook(bfStream);
		bfStream.close();

		Sheet cardSheet = wb.getSheet("Card");
		Sheet shieldSheet = wb.getSheet("Sheild");
		Sheet specialSheet = wb.getSheet("Special");
		ArrayList<Card> cardList = deck.getCardList();
		ArrayList<Shield> shieldList = deck.getShieldList();

		Row row;

		//カード情報の読み取り
		for(int i = 1; i <= cardSheet.getLastRowNum(); i++) {
			row = cardSheet.getRow(i);
			cardList.add(this.getCard(row.getCell(0).getStringCellValue()));
		}

		//シールド情報の読み取り
		BigDecimal life = BigDecimal.ONE;
		for(int i = 1; i <= shieldSheet.getLastRowNum(); i++) {
			row = shieldSheet.getRow(i);
			Shield shield = this.getShield(row.getCell(0).getStringCellValue());
			shieldList.add(shield);
			life = life.add(new BigDecimal(shield.getLife()));
		}

		//特殊情報の読み取り
		//特殊色
		deck.setSpecial(this.getSpecial(specialSheet.getRow(1).getCell(0).getStringCellValue()));

		//デッキライフ
		deck.setLife(life.toString());

		wb.close();

		return deck;
	}

	private HashMap<String, Integer> getCardColumIndex(Sheet cardSheet) {
		HashMap<String, Integer> columnMap= new HashMap<String, Integer>();

		columnMap = new HashMap<String, Integer>();
		//1行目を取得
		Row row = cardSheet.getRow(0);

		for(int i = 0; i < row.getLastCellNum(); i++ ) {
			columnMap.put(row.getCell(i).getStringCellValue(), new Integer(i));
		}

		return columnMap;
	}


	private String getStringCellValue(Cell cell) {
        Objects.requireNonNull(cell, "cell is null");

        CellType cellType = cell.getCellType();
        if (cellType == CellType.BLANK) {
            return null;
        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cellType == CellType.ERROR) {
            throw new RuntimeException("Error cell is unsupported");
        } else if (cellType == CellType.FORMULA) {
            throw new RuntimeException("Formula cell is unsupported");
        } else if (cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return String.valueOf(cell.getDateCellValue());
            } else {
                return String.valueOf(cell.getNumericCellValue());
            }
        } else if (cellType == CellType.STRING) {
            return cell.getStringCellValue();
        } else {
            throw new RuntimeException("Unknow type cell");
        }
    }
}
