package server;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleTrait;
import client.inventory.Equip;
import client.inventory.IItem;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ItemConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.Pair;

/**
 *
 * @author zjj
 */
public class MapleItemInformationProvider {

    private final static MapleItemInformationProvider instance = new MapleItemInformationProvider();

    /**
     *
     */
    protected Map<Integer, Boolean> onEquipUntradableCache = new HashMap<>();

    /**
     *
     */
    protected final MapleDataProvider etcData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Etc.wz"));

    /**
     *
     */
    protected final MapleDataProvider itemData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Item.wz"));

    /**
     *
     */
    protected final MapleDataProvider equipData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Character.wz"));

    /**
     *
     */
    protected final MapleDataProvider stringData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/String.wz"));

    /**
     *
     */
    protected final MapleData cashStringData = stringData.getData("Cash.img");

    /**
     *
     */
    protected final MapleData consumeStringData = stringData.getData("Consume.img");

    /**
     *
     */
    protected final MapleData eqpStringData = stringData.getData("Eqp.img");

    /**
     *
     */
    protected final MapleData etcStringData = stringData.getData("Etc.img");

    /**
     *
     */
    protected final MapleData insStringData = stringData.getData("Ins.img");

    /**
     *
     */
    protected final MapleData petStringData = stringData.getData("Pet.img");

    /**
     *
     */
    protected final Map<Integer, List<Integer>> scrollReqCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Short> slotMaxCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Integer> getExpCache = new HashMap();

    /**
     *
     */
    protected final Map<Integer, List<StructPotentialItem>> potentialCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, MapleStatEffect> itemEffects = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Map<String, Integer>> equipStatsCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Map<String, Byte>> itemMakeStatsCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Short> itemMakeLevel = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Equip> equipCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Double> priceCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Integer> wholePriceCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Integer> projectileWatkCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Integer> monsterBookID = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, String> nameCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, String> descCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, String> msgCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Map<String, Integer>> SkillStatsCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Byte> consumeOnPickupCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Boolean> dropRestrictionCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Boolean> accCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Boolean> pickupRestrictionCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Integer> stateChangeCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Integer> mesoCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Boolean> notSaleCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Integer> karmaEnabledCache = new HashMap<>();

    /**
     *
     */
    protected Map<Integer, Boolean> karmaCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Boolean> isQuestItemCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Boolean> blockPickupCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, List<Integer>> petsCanConsumeCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Boolean> logoutExpireCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, List<Pair<Integer, Integer>>> summonMobCache = new HashMap<>();

    /**
     *
     */
    protected final List<Pair<Integer, String>> itemNameCache = new ArrayList<>();

    /**
     *
     */
    protected final Map<Integer, Map<Integer, Map<String, Integer>>> equipIncsCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Map<Integer, List<Integer>>> equipSkillsCache = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Pair<Integer, List<StructRewardItem>>> RewardItem = new HashMap<>();

    /**
     *
     */
    protected final Map<Byte, StructSetItem> setItems = new HashMap<>();

    /**
     *
     */
    protected final Map<Integer, Pair<Integer, List<Integer>>> questItems = new HashMap<>();

    /**
     *
     */
    protected Map<Integer, MapleInventoryType> inventoryTypeCache = new HashMap();

    /**
     *
     */
    protected MapleItemInformationProvider() {
        System.out.println("ZeroMS服务端----------[加载物品信息]成功!");
    }

    /**
     *
     */
    public final void load() {
        if (!setItems.isEmpty() || !potentialCache.isEmpty()) {
            return;
        }
        getAllItems();
        /*
         * final MapleData setsData = etcData.getData("SetItemInfo.img");
         * StructSetItem itemz; SetItem itez; for (MapleData dat : setsData) {
         * itemz = new StructSetItem(); itemz.setItemID =
         * Byte.parseByte(dat.getName()); itemz.completeCount = (byte)
         * MapleDataTool.getIntConvert("completeCount", dat, 0); for (MapleData
         * level : dat.getChildByPath("ItemID")) {
         * itemz.itemIDs.add(MapleDataTool.getIntConvert(level)); } for
         * (MapleData level : dat.getChildByPath("Effect")) { itez = new
         * SetItem(); itez.incPDD = MapleDataTool.getIntConvert("incPDD", level,
         * 0); itez.incMDD = MapleDataTool.getIntConvert("incMDD", level, 0);
         * itez.incSTR = MapleDataTool.getIntConvert("incSTR", level, 0);
         * itez.incDEX = MapleDataTool.getIntConvert("incDEX", level, 0);
         * itez.incINT = MapleDataTool.getIntConvert("incINT", level, 0);
         * itez.incLUK = MapleDataTool.getIntConvert("incLUK", level, 0);
         * itez.incACC = MapleDataTool.getIntConvert("incACC", level, 0);
         * itez.incPAD = MapleDataTool.getIntConvert("incPAD", level, 0);
         * itez.incMAD = MapleDataTool.getIntConvert("incMAD", level, 0);
         * itez.incSpeed = MapleDataTool.getIntConvert("incSpeed", level, 0);
         * itez.incMHP = MapleDataTool.getIntConvert("incMHP", level, 0);
         * itez.incMMP = MapleDataTool.getIntConvert("incMMP", level, 0);
         * itemz.items.put(Integer.parseInt(level.getName()), itez); }
         * setItems.put(itemz.setItemID, itemz); }
         */
 /*
         * final MapleData potsData = itemData.getData("ItemOption.img");
         * StructPotentialItem item; List<StructPotentialItem> items; for
         * (MapleData dat : potsData) { items = new
         * LinkedList<StructPotentialItem>(); for (MapleData level :
         * dat.getChildByPath("level")) { item = new StructPotentialItem();
         * item.optionType = MapleDataTool.getIntConvert("info/optionType", dat,
         * 0); item.reqLevel = MapleDataTool.getIntConvert("info/reqLevel", dat,
         * 0); item.face = MapleDataTool.getString("face", level, ""); item.boss
         * = MapleDataTool.getIntConvert("boss", level, 0) > 0; item.potentialID
         * = Short.parseShort(dat.getName()); item.attackType = (short)
         * MapleDataTool.getIntConvert("attackType", level, 0); item.incMHP =
         * (short) MapleDataTool.getIntConvert("incMHP", level, 0); item.incMMP
         * = (short) MapleDataTool.getIntConvert("incMMP", level, 0);
         *
         * item.incSTR = (byte) MapleDataTool.getIntConvert("incSTR", level, 0);
         * item.incDEX = (byte) MapleDataTool.getIntConvert("incDEX", level, 0);
         * item.incINT = (byte) MapleDataTool.getIntConvert("incINT", level, 0);
         * item.incLUK = (byte) MapleDataTool.getIntConvert("incLUK", level, 0);
         * item.incACC = (byte) MapleDataTool.getIntConvert("incACC", level, 0);
         * item.incEVA = (byte) MapleDataTool.getIntConvert("incEVA", level, 0);
         * item.incSpeed = (byte) MapleDataTool.getIntConvert("incSpeed", level,
         * 0); item.incJump = (byte) MapleDataTool.getIntConvert("incJump",
         * level, 0); item.incPAD = (byte) MapleDataTool.getIntConvert("incPAD",
         * level, 0); item.incMAD = (byte) MapleDataTool.getIntConvert("incMAD",
         * level, 0); item.incPDD = (byte) MapleDataTool.getIntConvert("incPDD",
         * level, 0); item.incMDD = (byte) MapleDataTool.getIntConvert("incMDD",
         * level, 0); item.prop = (byte) MapleDataTool.getIntConvert("prop",
         * level, 0); item.time = (byte) MapleDataTool.getIntConvert("time",
         * level, 0); item.incSTRr = (byte)
         * MapleDataTool.getIntConvert("incSTRr", level, 0); item.incDEXr =
         * (byte) MapleDataTool.getIntConvert("incDEXr", level, 0); item.incINTr
         * = (byte) MapleDataTool.getIntConvert("incINTr", level, 0);
         * item.incLUKr = (byte) MapleDataTool.getIntConvert("incLUKr", level,
         * 0); item.incMHPr = (byte) MapleDataTool.getIntConvert("incMHPr",
         * level, 0); item.incMMPr = (byte)
         * MapleDataTool.getIntConvert("incMMPr", level, 0); item.incACCr =
         * (byte) MapleDataTool.getIntConvert("incACCr", level, 0); item.incEVAr
         * = (byte) MapleDataTool.getIntConvert("incEVAr", level, 0);
         * item.incPADr = (byte) MapleDataTool.getIntConvert("incPADr", level,
         * 0); item.incMADr = (byte) MapleDataTool.getIntConvert("incMADr",
         * level, 0); item.incPDDr = (byte)
         * MapleDataTool.getIntConvert("incPDDr", level, 0); item.incMDDr =
         * (byte) MapleDataTool.getIntConvert("incMDDr", level, 0); item.incCr =
         * (byte) MapleDataTool.getIntConvert("incCr", level, 0); item.incDAMr =
         * (byte) MapleDataTool.getIntConvert("incDAMr", level, 0);
         * item.RecoveryHP = (byte) MapleDataTool.getIntConvert("RecoveryHP",
         * level, 0); item.RecoveryMP = (byte)
         * MapleDataTool.getIntConvert("RecoveryMP", level, 0); item.HP = (byte)
         * MapleDataTool.getIntConvert("HP", level, 0); item.MP = (byte)
         * MapleDataTool.getIntConvert("MP", level, 0); item.level = (byte)
         * MapleDataTool.getIntConvert("level", level, 0); item.ignoreTargetDEF
         * = (byte) MapleDataTool.getIntConvert("ignoreTargetDEF", level, 0);
         * item.ignoreDAM = (byte) MapleDataTool.getIntConvert("ignoreDAM",
         * level, 0); item.DAMreflect = (byte)
         * MapleDataTool.getIntConvert("DAMreflect", level, 0); item.mpconReduce
         * = (byte) MapleDataTool.getIntConvert("mpconReduce", level, 0);
         * item.mpRestore = (byte) MapleDataTool.getIntConvert("mpRestore",
         * level, 0); item.incMesoProp = (byte)
         * MapleDataTool.getIntConvert("incMesoProp", level, 0);
         * item.incRewardProp = (byte)
         * MapleDataTool.getIntConvert("incRewardProp", level, 0);
         * item.incAllskill = (byte) MapleDataTool.getIntConvert("incAllskill",
         * level, 0); item.ignoreDAMr = (byte)
         * MapleDataTool.getIntConvert("ignoreDAMr", level, 0); item.RecoveryUP
         * = (byte) MapleDataTool.getIntConvert("RecoveryUP", level, 0); switch
         * (item.potentialID) { case 31001: case 31002: case 31003: case 31004:
         * item.skillID = (short) (item.potentialID - 23001); break; default:
         * item.skillID = 0; break; } items.add(item); }
         * potentialCache.put(Integer.parseInt(dat.getName()), items); }
         */
    }

    /**
     *
     * @param potId
     * @return
     */
    public final List<StructPotentialItem> getPotentialInfo(final int potId) {
        return potentialCache.get(potId);
    }

    /**
     *
     * @return
     */
    public final Map<Integer, List<StructPotentialItem>> getAllPotentialInfo() {
        return potentialCache;
    }

    /**
     *
     * @return
     */
    public static MapleItemInformationProvider getInstance() {
        return instance;
    }

    /**
     *
     * @return
     */
    public final List<Pair<Integer, String>> getAllItems() {
        if (!itemNameCache.isEmpty()) {
            return itemNameCache;
        }
        final List<Pair<Integer, String>> itemPairs = new ArrayList<>();
        MapleData itemsData;

        itemsData = stringData.getData("Cash.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Consume.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Eqp.img").getChildByPath("Eqp");
        for (final MapleData eqpType : itemsData.getChildren()) {
            for (final MapleData itemFolder : eqpType.getChildren()) {
                itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
            }
        }

        itemsData = stringData.getData("Etc.img").getChildByPath("Etc");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Ins.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Pet.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }
        return itemPairs;
    }

    /**
     *
     * @param itemId
     * @return
     */
    protected final MapleData getStringData(final int itemId) {
        String cat = null;
        MapleData data;

        if (itemId >= 5_010_000) {
            data = cashStringData;
        } else if (itemId >= 2_000_000 && itemId < 3_000_000) {
            data = consumeStringData;
        } else if ((itemId >= 1_142_000 && itemId < 1_143_000) || (itemId >= 1_010_000 && itemId < 1_040_000) || (itemId >= 1_122_000 && itemId < 1_123_000)) {
            data = eqpStringData;
            cat = "Accessory";
        } else if (itemId >= 1_000_000 && itemId < 1_010_000) {
            data = eqpStringData;
            cat = "Cap";
        } else if (itemId >= 1_102_000 && itemId < 1_103_000) {
            data = eqpStringData;
            cat = "Cape";
        } else if (itemId >= 1_040_000 && itemId < 1_050_000) {
            data = eqpStringData;
            cat = "Coat";
        } else if (itemId >= 20_000 && itemId < 25_000) {
            data = eqpStringData;
            cat = "Face";
        } else if (itemId >= 1_080_000 && itemId < 1_090_000) {
            data = eqpStringData;
            cat = "Glove";
        } else if (itemId >= 30_000 && itemId < 40_000) {
            data = eqpStringData;
            cat = "Hair";
        } else if (itemId >= 1_050_000 && itemId < 1_060_000) {
            data = eqpStringData;
            cat = "Longcoat";
        } else if (itemId >= 1_060_000 && itemId < 1_070_000) {
            data = eqpStringData;
            cat = "Pants";
        } else if (itemId >= 1_610_000 && itemId < 1_660_000) {
            data = eqpStringData;
            cat = "Mechanic";
        } else if (itemId >= 1_802_000 && itemId < 1_810_000) {
            data = eqpStringData;
            cat = "PetEquip";
        } else if (itemId >= 1_920_000 && itemId < 2_000_000) {
            data = eqpStringData;
            cat = "Dragon";
        } else if (itemId >= 1_112_000 && itemId < 1_120_000) {
            data = eqpStringData;
            cat = "Ring";
        } else if (itemId >= 1_092_000 && itemId < 1_100_000) {
            data = eqpStringData;
            cat = "Shield";
        } else if (itemId >= 1_070_000 && itemId < 1_080_000) {
            data = eqpStringData;
            cat = "Shoes";
        } else if (itemId >= 1_900_000 && itemId < 1_920_000) {
            data = eqpStringData;
            cat = "Taming";
        } else if (itemId >= 1_300_000 && itemId < 1_800_000) {
            data = eqpStringData;
            cat = "Weapon";
        } else if (itemId >= 4_000_000 && itemId < 5_000_000) {
            data = etcStringData;
        } else if (itemId >= 3_000_000 && itemId < 4_000_000) {
            data = insStringData;
        } else if (itemId >= 5_000_000 && itemId < 5_010_000) {
            data = petStringData;
        } else {
            return null;
        }
        if (cat == null) {
            return data.getChildByPath(String.valueOf(itemId));
        } else {
            return data.getChildByPath("Eqp/" + cat + "/" + itemId);
        }
    }

    /**
     *
     * @param itemId
     * @return
     */
    protected final MapleData getItemData(final int itemId) {
        MapleData ret = null;
        final String idStr = "0" + String.valueOf(itemId);
        MapleDataDirectoryEntry root = itemData.getRoot();
        for (final MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            // we should have .img files here beginning with the first 4 IID
            for (final MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr.substring(0, 4) + ".img")) {
                    ret = itemData.getData(topDir.getName() + "/" + iFile.getName());
                    if (ret == null) {
                        return null;
                    }
                    ret = ret.getChildByPath(idStr);
                    return ret;
                } else if (iFile.getName().equals(idStr.substring(1) + ".img")) {
                    return itemData.getData(topDir.getName() + "/" + iFile.getName());
                }
            }
        }
        root = equipData.getRoot();
        for (final MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            for (final MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr + ".img")) {
                    return equipData.getData(topDir.getName() + "/" + iFile.getName());
                }
            }
        }
        return ret;
    }

    /**
     * returns the maximum of items in one slot
     */
    public final short getSlotMax(final MapleClient c, final int itemId) {
        if (slotMaxCache.containsKey(itemId)) {
            return slotMaxCache.get(itemId);
        }
        short ret = 0;
        final MapleData item = getItemData(itemId);
        if (item != null) {
            final MapleData smEntry = item.getChildByPath("info/slotMax");
            if (smEntry == null) {
                if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    ret = 1;
                } else {
                    ret = 100;
                }
            } else {
                ret = (short) MapleDataTool.getInt(smEntry);
            }
        }
        slotMaxCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final int getWholePrice(final int itemId) {
        if (wholePriceCache.containsKey(itemId)) {
            return wholePriceCache.get(itemId);
        }
        final MapleData item = getItemData(itemId);
        if (item == null) {
            return -1;
        }
        int pEntry = 0;
        final MapleData pData = item.getChildByPath("info/price");
        if (pData == null) {
            return -1;
        }
        pEntry = MapleDataTool.getInt(pData);

        wholePriceCache.put(itemId, pEntry);
        return pEntry;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final double getPrice(final int itemId) {

        if (priceCache.containsKey(itemId)) {
            return priceCache.get(itemId);
        }
        final MapleData item = getItemData(itemId);
        if (item == null) {
            return -1;
        }
        double pEntry = 0.0;
        MapleData pData = item.getChildByPath("info/unitPrice");
        if (pData != null) {
            try {
                pEntry = MapleDataTool.getDouble(pData);
            } catch (Exception e) {
                pEntry = (double) MapleDataTool.getIntConvert(pData);
            }
        } else {
            pData = item.getChildByPath("info/price");
            if (pData == null) {
                return -1;
            }
            pEntry = (double) MapleDataTool.getIntConvert(pData);
        }
        if (itemId == 2_070_019 || itemId == 2_330_007) {
            pEntry = 1.0;
        }
        priceCache.put(itemId, pEntry);
        return pEntry;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final Map<String, Byte> getItemMakeStats(final int itemId) {
        if (itemMakeStatsCache.containsKey(itemId)) {
            return itemMakeStatsCache.get(itemId);
        }
        if (itemId / 10_000 != 425) {
            return null;
        }
        final Map<String, Byte> ret = new LinkedHashMap<>();
        final MapleData item = getItemData(itemId);
        if (item == null) {
            return null;
        }
        final MapleData info = item.getChildByPath("info");
        if (info == null) {
            return null;
        }
        ret.put("incPAD", (byte) MapleDataTool.getInt("incPAD", info, 0)); // WATK
        ret.put("incMAD", (byte) MapleDataTool.getInt("incMAD", info, 0)); // MATK
        ret.put("incACC", (byte) MapleDataTool.getInt("incACC", info, 0)); // ACC
        ret.put("incEVA", (byte) MapleDataTool.getInt("incEVA", info, 0)); // AVOID
        ret.put("incSpeed", (byte) MapleDataTool.getInt("incSpeed", info, 0)); // SPEED
        ret.put("incJump", (byte) MapleDataTool.getInt("incJump", info, 0)); // JUMP
        ret.put("incMaxHP", (byte) MapleDataTool.getInt("incMaxHP", info, 0)); // HP
        ret.put("incMaxMP", (byte) MapleDataTool.getInt("incMaxMP", info, 0)); // MP
        ret.put("incSTR", (byte) MapleDataTool.getInt("incSTR", info, 0)); // STR
        ret.put("incINT", (byte) MapleDataTool.getInt("incINT", info, 0)); // INT
        ret.put("incLUK", (byte) MapleDataTool.getInt("incLUK", info, 0)); // LUK
        ret.put("incDEX", (byte) MapleDataTool.getInt("incDEX", info, 0)); // DEX
//	ret.put("incReqLevel", MapleDataTool.getInt("incReqLevel", info, 0)); // IDK!
        ret.put("randOption", (byte) MapleDataTool.getInt("randOption", info, 0)); // Black Crystal Wa/MA
        ret.put("randStat", (byte) MapleDataTool.getInt("randStat", info, 0)); // Dark Crystal - Str/Dex/int/Luk

        itemMakeStatsCache.put(itemId, ret);
        return ret;
    }

    private int rand(int min, int max) {
        return Math.abs((int) Randomizer.rand(min, max));
    }

    /**
     *
     * @param equip
     * @param sta
     * @return
     */
    public Equip levelUpEquip(Equip equip, Map<String, Integer> sta) {
        Equip nEquip = (Equip) equip.copy();
        //is this all the stats?
        try {
            for (Entry<String, Integer> stat : sta.entrySet()) {
                switch (stat.getKey()) {
                    case "STRMin":
                        nEquip.setStr((short) (nEquip.getStr() + rand(stat.getValue(), sta.get("STRMax"))));
                        break;
                    case "DEXMin":
                        nEquip.setDex((short) (nEquip.getDex() + rand(stat.getValue(), sta.get("DEXMax"))));
                        break;
                    case "INTMin":
                        nEquip.setInt((short) (nEquip.getInt() + rand(stat.getValue(), sta.get("INTMax"))));
                        break;
                    case "LUKMin":
                        nEquip.setLuk((short) (nEquip.getLuk() + rand(stat.getValue(), sta.get("LUKMax"))));
                        break;
                    case "PADMin":
                        nEquip.setWatk((short) (nEquip.getWatk() + rand(stat.getValue(), sta.get("PADMax"))));
                        break;
                    case "PDDMin":
                        nEquip.setWdef((short) (nEquip.getWdef() + rand(stat.getValue(), sta.get("PDDMax"))));
                        break;
                    case "MADMin":
                        nEquip.setMatk((short) (nEquip.getMatk() + rand(stat.getValue(), sta.get("MADMax"))));
                        break;
                    case "MDDMin":
                        nEquip.setMdef((short) (nEquip.getMdef() + rand(stat.getValue(), sta.get("MDDMax"))));
                        break;
                    case "ACCMin":
                        nEquip.setAcc((short) (nEquip.getAcc() + rand(stat.getValue(), sta.get("ACCMax"))));
                        break;
                    case "EVAMin":
                        nEquip.setAvoid((short) (nEquip.getAvoid() + rand(stat.getValue(), sta.get("EVAMax"))));
                        break;
                    case "SpeedMin":
                        nEquip.setSpeed((short) (nEquip.getSpeed() + rand(stat.getValue(), sta.get("SpeedMax"))));
                        break;
                    case "JumpMin":
                        nEquip.setJump((short) (nEquip.getJump() + rand(stat.getValue(), sta.get("JumpMax"))));
                        break;
                    case "MHPMin":
                        nEquip.setHp((short) (nEquip.getHp() + rand(stat.getValue(), sta.get("MHPMax"))));
                        break;
                    case "MMPMin":
                        nEquip.setMp((short) (nEquip.getMp() + rand(stat.getValue(), sta.get("MMPMax"))));
                        break;
                    case "MaxHPMin":
                        nEquip.setHp((short) (nEquip.getHp() + rand(stat.getValue(), sta.get("MaxHPMax"))));
                        break;
                    case "MaxMPMin":
                        nEquip.setMp((short) (nEquip.getMp() + rand(stat.getValue(), sta.get("MaxMPMax"))));
                        break;
                    default:
                        break;
                }
            }
        } catch (NullPointerException e) {
            //catch npe because obviously the wz have some error XD
            e.printStackTrace();
        }
        return nEquip;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final Map<Integer, Map<String, Integer>> getEquipIncrements(final int itemId) {
        if (equipIncsCache.containsKey(itemId)) {
            return equipIncsCache.get(itemId);
        }
        final Map<Integer, Map<String, Integer>> ret = new LinkedHashMap<>();
        final MapleData item = getItemData(itemId);
        if (item == null) {
            return null;
        }
        final MapleData info = item.getChildByPath("info/level/info");
        if (info == null) {
            return null;
        }
        for (MapleData dat : info.getChildren()) {
            Map<String, Integer> incs = new HashMap<>();
            for (MapleData data : dat.getChildren()) { //why we have to do this? check if number has skills or not
                if (data.getName().length() > 3) {
                    incs.put(data.getName().substring(3), MapleDataTool.getIntConvert(data.getName(), dat, 0));
                }
            }
            ret.put(Integer.parseInt(dat.getName()), incs);
        }
        equipIncsCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final Map<Integer, List<Integer>> getEquipSkills(final int itemId) {
        if (equipSkillsCache.containsKey(itemId)) {
            return equipSkillsCache.get(itemId);
        }
        final Map<Integer, List<Integer>> ret = new LinkedHashMap<>();
        final MapleData item = getItemData(itemId);
        if (item == null) {
            return null;
        }
        final MapleData info = item.getChildByPath("info/level/case");
        if (info == null) {
            return null;
        }
        for (MapleData dat : info.getChildren()) {
            for (MapleData data : dat.getChildren()) { //why we have to do this? check if number has skills or not
                if (data.getName().length() == 1) { //the numbers all them are one digit. everything else isnt so we're lucky here..
                    List<Integer> adds = new ArrayList<>();
                    for (MapleData skil : data.getChildByPath("Skill").getChildren()) {
                        adds.add(MapleDataTool.getIntConvert("id", skil, 0));
                    }
                    ret.put(Integer.parseInt(data.getName()), adds);
                }
            }
        }
        equipSkillsCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final Map<String, Integer> getEquipStats(final int itemId) {
        if (equipStatsCache.containsKey(itemId)) {
            return equipStatsCache.get(itemId);
        }
        final Map<String, Integer> ret = new LinkedHashMap<>();
        final MapleData item = getItemData(itemId);
        if (item == null) {
            return null;
        }
        final MapleData info = item.getChildByPath("info");
        if (info == null) {
            return null;
        }
        for (final MapleData data : info.getChildren()) {
            if (data.getName().startsWith("inc")) {
                ret.put(data.getName().substring(3), MapleDataTool.getIntConvert(data));
            }
        }
        ret.put("tuc", MapleDataTool.getInt("tuc", info, 0));
        ret.put("reqLevel", MapleDataTool.getInt("reqLevel", info, 0));
        ret.put("reqJob", MapleDataTool.getInt("reqJob", info, 0));
        ret.put("reqSTR", MapleDataTool.getInt("reqSTR", info, 0));
        ret.put("reqDEX", MapleDataTool.getInt("reqDEX", info, 0));
        ret.put("reqINT", MapleDataTool.getInt("reqINT", info, 0));
        ret.put("reqLUK", MapleDataTool.getInt("reqLUK", info, 0));
        ret.put("reqPOP", MapleDataTool.getInt("reqPOP", info, 0));
        ret.put("cash", MapleDataTool.getInt("cash", info, 0));
        ret.put("canLevel", info.getChildByPath("level") == null ? 0 : 1);
        ret.put("cursed", MapleDataTool.getInt("cursed", info, 0));
        ret.put("success", MapleDataTool.getInt("success", info, 0));
        ret.put("successRates", MapleDataTool.getInt("successRates", info, 0));
        ret.put("setItemID", MapleDataTool.getInt("setItemID", info, 0));
        ret.put("equipTradeBlock", MapleDataTool.getInt("equipTradeBlock", info, 0));
        ret.put("durability", MapleDataTool.getInt("durability", info, -1));

        if (GameConstants.isMagicWeapon(itemId)) {
            ret.put("elemDefault", MapleDataTool.getInt("elemDefault", info, 100));
            ret.put("incRMAS", MapleDataTool.getInt("incRMAS", info, 100)); // Poison
            ret.put("incRMAF", MapleDataTool.getInt("incRMAF", info, 100)); // Fire
            ret.put("incRMAL", MapleDataTool.getInt("incRMAL", info, 100)); // Lightning
            ret.put("incRMAI", MapleDataTool.getInt("incRMAI", info, 100)); // Ice
        }

        equipStatsCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param stats
     * @param itemid
     * @param level
     * @param job
     * @param fame
     * @param str
     * @param dex
     * @param luk
     * @param int_
     * @param supremacy
     * @return
     */
    public final boolean canEquip(final Map<String, Integer> stats, final int itemid, final int level, final int job, final int fame, final int str, final int dex, final int luk, final int int_, final int supremacy) {
        if ((level + supremacy) >= stats.get("reqLevel") && str >= stats.get("reqSTR") && dex >= stats.get("reqDEX") && luk >= stats.get("reqLUK") && int_ >= stats.get("reqINT")) {
            final int fameReq = stats.get("reqPOP");
            return !(fameReq != 0 && fame < fameReq);
        }
        return false;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final int getReqLevel(final int itemId) {//判断装备等级
        if (getEquipStats(itemId) == null) {
            return 0;
        }
        return getEquipStats(itemId).get("reqLevel");
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean isCashItem(final int itemId) {
        if (getEquipStats(itemId) == null) {
            return false;
        }
        return getEquipStats(itemId).get("cash") == 1;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final int getSlots(final int itemId) {
        if (getEquipStats(itemId) == null) {
            return 0;
        }
        return getEquipStats(itemId).get("tuc");
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final int getSetItemID(final int itemId) {
        if (getEquipStats(itemId) == null) {
            return 0;
        }
        return getEquipStats(itemId).get("setItemID");
    }

    /**
     *
     * @param setItemId
     * @return
     */
    public final StructSetItem getSetItem(final int setItemId) {
        return setItems.get((byte) setItemId);
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final List<Integer> getScrollReqs(final int itemId) {
        if (scrollReqCache.containsKey(itemId)) {
            return scrollReqCache.get(itemId);
        }
        final List<Integer> ret = new ArrayList<>();
        final MapleData data = getItemData(itemId).getChildByPath("req");

        if (data == null) {
            return ret;
        }
        for (final MapleData req : data.getChildren()) {
            ret.add(MapleDataTool.getInt(req));
        }
        scrollReqCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public int getScrollSuccess(int itemId) {
        return getScrollSuccess(itemId, 0);
    }

    /**
     *
     * @param itemId
     * @param def
     * @return
     */
    public int getScrollSuccess(int itemId, int def) {
        return getEquipStats(itemId).get("success");

    }

    /**
     *
     * @param itemId
     * @return
     */
    public int getScrollCursed(int itemId) {
        return getEquipStats(itemId).get("cursed");
    }

    /**
     *
     * @param equip
     * @param scrollId
     * @param ws
     * @param chr
     * @param vegas
     * @param checkIfGM
     * @return
     */
    public final IItem scrollEquipWithId(final IItem equip, final IItem scrollId, final boolean ws, final MapleCharacter chr, final int vegas, boolean checkIfGM) {
        if (equip.getType() == 1) { // See IItem.java

            final Equip nEquip = (Equip) equip;
            final Map<String, Integer> stats = getEquipStats(scrollId.getItemId());
            final Map<String, Integer> eqstats = getEquipStats(equip.getItemId());
            final int succ = (GameConstants.isTablet(scrollId.getItemId()) ? GameConstants.getSuccessTablet(scrollId.getItemId(), nEquip.getLevel()) : ((GameConstants.isEquipScroll(scrollId.getItemId()) || GameConstants.isPotentialScroll(scrollId.getItemId()) ? 0 : stats.get("success"))));
            final int curse = (GameConstants.isTablet(scrollId.getItemId()) ? GameConstants.getCurseTablet(scrollId.getItemId(), nEquip.getLevel()) : ((GameConstants.isEquipScroll(scrollId.getItemId()) || GameConstants.isPotentialScroll(scrollId.getItemId()) ? 0 : stats.get("cursed"))));
            //  final int added = (ItemFlag.幸运卷轴.check(equip.getFlag()) ? 10 : 0) + (chr.getTrait(MapleTrait.MapleTraitType.craft).getLevel() / 10); 
            //int craft = GameConstants.is白医卷轴(scrollId.getItemId()) ? 0 : chr.getTrait(MapleTraitType.craft).getLevel() / 10;
            //lucksKey--辛运日加成
            int lucksKey = ItemFlag.幸运卷轴.check(equip.getFlag()) ? 10 : 0;
            //added -- 额外成功率
            //   int added = (GameConstants.is潜能卷轴(scrollId.getItemId())) || (GameConstants.is强化卷轴(scrollId.getItemId())) ? 0 : lucksKey + craft;
            //success--总成功概率
            int success = succ + (vegas == 5_610_000 && succ == 10 ? 20 : (vegas == 5_610_001 && succ == 60 ? 30 : 0));
            //使用卷轴后如果有幸运日效果则减去
            /*   if ((ItemFlag.幸运卷轴.check(equip.getFlag())) && (!GameConstants.is潜能卷轴(scrollId.getItemId())) && (!GameConstants.is强化卷轴(scrollId.getItemId())) && (!GameConstants.is特殊卷轴(scrollId.getItemId()))) {
                equip.setFlag((byte) (equip.getFlag() - ItemFlag.幸运卷轴.getValue()));
            }*/
            if (GameConstants.isPotentialScroll(scrollId.getItemId()) || GameConstants.isEquipScroll(scrollId.getItemId()) || Randomizer.nextInt(100) <= success || checkIfGM == true) {
                switch (scrollId.getItemId()) {
                    case 2_049_000:
                    case 2_049_001:
                    case 2_049_002:
                    case 2_049_003:
                    case 2_049_004:
                    case 2_049_005: {
                        if (nEquip.getLevel() + nEquip.getUpgradeSlots() < eqstats.get("tuc")) {
                            nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 1));
                        }
                        break;
                    }
                    case 2_049_006:
                    case 2_049_007:
                    case 2_049_008: {
                        if (nEquip.getLevel() + nEquip.getUpgradeSlots() < eqstats.get("tuc")) {
                            nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 2));
                        }
                        break;
                    }
                    case 2_040_727: // Spikes on shoe, prevents slip
                    {
                        byte flag = nEquip.getFlag();
                        flag |= ItemFlag.SPIKES.getValue();
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 2_041_058: // Cape for Cold protection
                    {
                        byte flag = nEquip.getFlag();
                        flag |= ItemFlag.COLD.getValue();
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 2_530_000: {
                        byte flag = nEquip.getFlag();
                        flag |= ItemFlag.幸运卷轴.getValue();
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 2_530_001: {
                        byte flag = nEquip.getFlag();
                        flag = (byte) (flag | ItemFlag.幸运卷轴.getValue());
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 2_530_002: {
                        byte flag = nEquip.getFlag();
                        flag = (byte) (flag | ItemFlag.幸运卷轴.getValue());
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 5_063_100: {
                        byte flag = nEquip.getFlag();
                        flag = (byte) (flag | ItemFlag.幸运卷轴.getValue());
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 2_531_000:
                    case 5_064_000: {
                        byte flag = nEquip.getFlag();
                        flag = (byte) (flag | ItemFlag.防爆卷轴.getValue());
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 5_064_100: {
                        byte flag = nEquip.getFlag();
                        flag = (byte) (flag | ItemFlag.保护卷轴.getValue());
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 5_064_300: {
                        byte flag = nEquip.getFlag();
                        flag = (byte) (flag | ItemFlag.防护卷轴.getValue());
                        nEquip.setFlag(flag);
                        break;
                    }
                    default: {
                        if (ItemConstants.isChaosForGoodness(scrollId.getItemId())) {//正向混沌
                            final int z = GameConstants.getChaosNumber(scrollId.getItemId());
                            if (nEquip.getStr() > 0) {
                                nEquip.setStr((short) (nEquip.getStr() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getDex() > 0) {
                                nEquip.setDex((short) (nEquip.getDex() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getInt() > 0) {
                                nEquip.setInt((short) (nEquip.getInt() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getLuk() > 0) {
                                nEquip.setLuk((short) (nEquip.getLuk() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getWatk() > 0) {
                                nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getWdef() > 0) {
                                nEquip.setWdef((short) (nEquip.getWdef() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getMatk() > 0) {
                                nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getMdef() > 0) {
                                nEquip.setMdef((short) (nEquip.getMdef() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getAcc() > 0) {
                                nEquip.setAcc((short) (nEquip.getAcc() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getAvoid() > 0) {
                                nEquip.setAvoid((short) (nEquip.getAvoid() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getSpeed() > 0) {
                                nEquip.setSpeed((short) (nEquip.getSpeed() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getJump() > 0) {
                                nEquip.setJump((short) (nEquip.getJump() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getHp() > 0) {
                                nEquip.setHp((short) (nEquip.getHp() + Randomizer.nextInt(z)));
                            }
                            if (nEquip.getMp() > 0) {
                                nEquip.setMp((short) (nEquip.getMp() + Randomizer.nextInt(z)));
                            }
                            break;
                        } else if (GameConstants.isChaosScroll(scrollId.getItemId())) {//枫叶卷轴
                            final int z = GameConstants.getChaosNumber(scrollId.getItemId());
                            if (nEquip.getStr() > 0) {
                                nEquip.setStr((short) (nEquip.getStr() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getDex() > 0) {
                                nEquip.setDex((short) (nEquip.getDex() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getInt() > 0) {
                                nEquip.setInt((short) (nEquip.getInt() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getLuk() > 0) {
                                nEquip.setLuk((short) (nEquip.getLuk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getWatk() > 0) {
                                nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getWdef() > 0) {
                                nEquip.setWdef((short) (nEquip.getWdef() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getMatk() > 0) {
                                nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getMdef() > 0) {
                                nEquip.setMdef((short) (nEquip.getMdef() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getAcc() > 0) {
                                nEquip.setAcc((short) (nEquip.getAcc() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getAvoid() > 0) {
                                nEquip.setAvoid((short) (nEquip.getAvoid() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getSpeed() > 0) {
                                nEquip.setSpeed((short) (nEquip.getSpeed() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getJump() > 0) {
                                nEquip.setJump((short) (nEquip.getJump() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getHp() > 0) {
                                nEquip.setHp((short) (nEquip.getHp() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getMp() > 0) {
                                nEquip.setMp((short) (nEquip.getMp() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            break;
                        } else if (GameConstants.is周年庆卷轴(scrollId.getItemId())) {

                            //  int 成功率 = GameConstants.is周年庆卷轴成功率(scrollId.getItemId()) + added;
                            //下面这句 是 使用卷轴失败
                            if (Randomizer.nextInt(100) <= succ) {
                                return null;
                            }

                            switch (scrollId.getItemId()) {
                                //+7 物攻
                                case 2_046_006:
                                case 2_046_008:
                                case 2_046_010:
                                case 2_046_106:
                                case 2_046_108:
                                case 2_046_110:
                                case 2_046_119:
                                    if (nEquip.getWatk() >= 0) {
                                        nEquip.setWatk((short) (nEquip.getWatk() + 7));
                                    }
                                    break;

                                // +7 魔攻
                                case 2_046_007:
                                case 2_046_026:
                                case 2_046_107:
                                case 2_046_120:
                                    if (nEquip.getMatk() >= 0) {
                                        nEquip.setMatk((short) (nEquip.getMatk() + 7));
                                    }
                                    break;
                                //最大HP+200，最大MP+200
                                case 2_046_213://周年庆防具强化卷轴
                                case 2_046_308://周年庆饰品强化卷轴
                                    if ((nEquip.getHp() >= 0) || (nEquip.getMp() >= 0)) {
                                        nEquip.setHp((short) (nEquip.getHp() + 200));
                                        nEquip.setMp((short) (nEquip.getMp() + 200));

                                    }
                                    break;
                                //力量+2，智力+2，敏捷+2，运气+2
                                case 2_046_214:
                                case 2_046_222:
                                case 2_046_309:
                                case 2_046_313:
                                    if ((nEquip.getStr() >= 0) || (nEquip.getDex() >= 0) || (nEquip.getInt() >= 0) || (nEquip.getLuk() >= 0)) {
                                        nEquip.setStr((short) (nEquip.getStr() + 2));
                                        nEquip.setDex((short) (nEquip.getDex() + 2));
                                        nEquip.setInt((short) (nEquip.getInt() + 2));
                                        nEquip.setLuk((short) (nEquip.getLuk() + 2));

                                    }
                                    break;
                                //MaxHP +70, MaxMP +70
                                case 2_046_219:
                                case 2_046_221:
                                case 2_046_310:
                                case 2_046_312:
                                    if ((nEquip.getHp() >= 0) || (nEquip.getMp() >= 0)) {
                                        nEquip.setHp((short) (nEquip.getHp() + 70));
                                        nEquip.setMp((short) (nEquip.getMp() + 70));

                                    }
                                    break;
                                case 2_043_108://【周年庆】单手斧攻击卷轴   物理攻击力+3,力量+2\
                                case 2_043_208://【周年庆】单手钝器攻击卷轴
                                case 2_044_008://【周年庆】双手剑攻击卷轴
                                case 2_044_108://【周年庆】双手斧攻击卷轴
                                case 2_044_208://【周年庆】双手钝器攻击卷轴
                                case 2_044_308://【周年庆】枪攻击卷轴
                                case 2_044_408://【周年庆】矛攻击卷轴
                                case 2_044_810://【周年庆】指节攻击卷轴40%
                                    if ((nEquip.getWatk() >= 0) || nEquip.getStr() >= 0) {
                                        nEquip.setWatk((short) (nEquip.getWatk() + 3));
                                        nEquip.setStr((short) (nEquip.getStr() + 2));
                                    }
                                    break;

                                case 2_043_308://【周年庆】短剑攻击卷轴  物理攻击力+3,运气+2\
                                case 2_043_405://【周年庆】刀攻击力卷轴
                                    if ((nEquip.getWatk() >= 0) || nEquip.getLuk() >= 0) {
                                        nEquip.setWatk((short) (nEquip.getWatk() + 3));
                                        nEquip.setLuk((short) (nEquip.getLuk() + 2));
                                    }
                                    break;

                                case 2_043_708://【周年庆】短杖魔力卷轴  
                                case 2_043_808://【周年庆】长杖魔力卷轴   魔法攻击力+3，智力+2
                                    if ((nEquip.getMatk() >= 0) || nEquip.getInt() >= 0) {
                                        nEquip.setMatk((short) (nEquip.getMatk() + 3));
                                        nEquip.setInt((short) (nEquip.getInt() + 2));
                                    }
                                    break;

                                case 2_044_508://【周年庆】弓攻击卷轴  命中值+20
                                    if ((nEquip.getWatk() >= 0) || nEquip.getAcc() >= 0) {
                                        nEquip.setWatk((short) (nEquip.getWatk() + 3));
                                        nEquip.setAcc((short) (nEquip.getAcc() + 20));
                                    }
                                    break;

                                case 2_044_608://【周年庆】弩攻击卷轴  命中值+40
                                case 2_044_708://【周年庆】拳套攻击卷轴
                                case 2_044_905://【周年庆】短枪攻击卷轴40%
                                    if ((nEquip.getWatk() >= 0) || nEquip.getAcc() >= 0) {
                                        nEquip.setWatk((short) (nEquip.getWatk() + 3));
                                        nEquip.setAcc((short) (nEquip.getAcc() + 40));
                                    }
                                    break;

                            }

                        } else if (GameConstants.isChaosScroll(scrollId.getItemId())) {
                            final int z = GameConstants.getChaosNumber(scrollId.getItemId());
                            if (nEquip.getStr() > 0) {
                                nEquip.setStr((short) (nEquip.getStr() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getDex() > 0) {
                                nEquip.setDex((short) (nEquip.getDex() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getInt() > 0) {
                                nEquip.setInt((short) (nEquip.getInt() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getLuk() > 0) {
                                nEquip.setLuk((short) (nEquip.getLuk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getWatk() > 0) {
                                nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getWdef() > 0) {
                                nEquip.setWdef((short) (nEquip.getWdef() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getMatk() > 0) {
                                nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getMdef() > 0) {
                                nEquip.setMdef((short) (nEquip.getMdef() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getAcc() > 0) {
                                nEquip.setAcc((short) (nEquip.getAcc() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getAvoid() > 0) {
                                nEquip.setAvoid((short) (nEquip.getAvoid() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getSpeed() > 0) {
                                nEquip.setSpeed((short) (nEquip.getSpeed() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getJump() > 0) {
                                nEquip.setJump((short) (nEquip.getJump() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getHp() > 0) {
                                nEquip.setHp((short) (nEquip.getHp() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getMp() > 0) {
                                nEquip.setMp((short) (nEquip.getMp() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            break;
                        } else if (GameConstants.isEquipScroll(scrollId.getItemId())) {
                            final int chanc = Math.max((scrollId.getItemId() == 2_049_300 || scrollId.getItemId() == 2_049_303 || scrollId.getItemId() == 2_049_306 ? 100 : (scrollId.getItemId() == 2_049_308 ? 50 : scrollId.getItemId() == 2_049_305 ? 60 : 80)) - (nEquip.getEnhance() * 10), 10);
                            if (Randomizer.nextInt(100) > chanc) {
                                return null; //destroyed, nib
                            }
                            for (int i = 0; i < (scrollId.getItemId() == 2_049_308 ? 5 : (scrollId.getItemId() == 2_049_305 ? 4 : (scrollId.getItemId() == 2_049_304 ? 3 : scrollId.getItemId() == 2_049_309 ? 2 : 1))); i++) {
                                if (nEquip.getStr() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                    nEquip.setStr((short) (nEquip.getStr() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getStr() > 0 && (GameConstants.isTyrant(nEquip.getItemId()) || Randomizer.nextInt(1) == 1)) {
                                    nEquip.setStr((short) (nEquip.getStr() + 22));
                                }
                                if (nEquip.getStr() > 0 && (GameConstants.isNovaGear(nEquip.getItemId()) || Randomizer.nextInt(1) == 1)) {
                                    nEquip.setStr((short) (nEquip.getStr() + 12));
                                }
                                if (nEquip.getDex() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                    nEquip.setDex((short) (nEquip.getDex() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getDex() > 0 && (GameConstants.isTyrant(nEquip.getItemId()) || Randomizer.nextInt(1) == 1)) {
                                    nEquip.setDex((short) (nEquip.getDex() + 22));
                                }
                                if (nEquip.getDex() > 0 && (GameConstants.isNovaGear(nEquip.getItemId()) || Randomizer.nextInt(1) == 1)) {
                                    nEquip.setDex((short) (nEquip.getDex() + 12));
                                }
                                if (nEquip.getInt() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                    nEquip.setInt((short) (nEquip.getInt() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getInt() > 0 && (GameConstants.isTyrant(nEquip.getItemId()) || Randomizer.nextInt(1) == 1)) {
                                    nEquip.setInt((short) (nEquip.getInt() + 22));
                                }
                                if (nEquip.getInt() > 0 && (GameConstants.isTyrant(nEquip.getItemId()) || Randomizer.nextInt(1) == 1)) {
                                    nEquip.setInt((short) (nEquip.getInt() + 12));
                                }
                                if (nEquip.getLuk() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                    nEquip.setLuk((short) (nEquip.getLuk() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getLuk() > 0 && (GameConstants.isTyrant(nEquip.getItemId()) || Randomizer.nextInt(1) == 1)) {
                                    nEquip.setLuk((short) (nEquip.getLuk() + 22));
                                }
                                if (nEquip.getLuk() > 0 && (GameConstants.isNovaGear(nEquip.getItemId()) || Randomizer.nextInt(1) == 1)) {
                                    nEquip.setLuk((short) (nEquip.getLuk() + 12));
                                }
                                if (nEquip.getWatk() > 0 && GameConstants.isWeapon(nEquip.getItemId())) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + (nEquip.getWatk() / 50 + 1)));
                                }
                                if (nEquip.getWatk() > 0 && !GameConstants.isWeapon(nEquip.getItemId()) && Randomizer.nextInt(5) == 1 || Randomizer.nextInt(5) == 1 && !GameConstants.isWeapon(nEquip.getItemId())) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getWatk() > 0 && GameConstants.isTyrant(nEquip.getItemId()) && nEquip.getEnhance() > 3) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + 5));
                                }
                                if (nEquip.getWatk() > 0 && GameConstants.isNovaGear(nEquip.getItemId()) && nEquip.getEnhance() > 5) {
                                    nEquip.setWatk((short) (nEquip.getWatk() + 2));
                                }
                                if (nEquip.getWdef() > 0 || Randomizer.nextInt(40) == 1) { //1/40
                                    nEquip.setWdef((short) (nEquip.getWdef() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getMatk() > 0 && GameConstants.isWeapon(nEquip.getItemId())) {
                                    nEquip.setMatk((short) (nEquip.getMatk() + (nEquip.getMatk() / 50 + 1)));
                                }
                                if (nEquip.getMatk() > 0 && GameConstants.isTyrant(nEquip.getItemId()) && nEquip.getEnhance() > 3) {
                                    nEquip.setMatk((short) (nEquip.getMatk() + 5));
                                }
                                if (nEquip.getMatk() > 0 && GameConstants.isNovaGear(nEquip.getItemId()) && nEquip.getEnhance() > 5) {
                                    nEquip.setMatk((short) (nEquip.getMatk() + 2));
                                }
                                if (nEquip.getMdef() > 0 || Randomizer.nextInt(40) == 1) { //1/40
                                    nEquip.setMdef((short) (nEquip.getMdef() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getAcc() > 0 || Randomizer.nextInt(20) == 1) { //1/20
                                    nEquip.setAcc((short) (nEquip.getAcc() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getAvoid() > 0 || Randomizer.nextInt(20) == 1) { //1/20
                                    nEquip.setAvoid((short) (nEquip.getAvoid() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getSpeed() > 0 || Randomizer.nextInt(10) == 1) { //1/10
                                    nEquip.setSpeed((short) (nEquip.getSpeed() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getJump() > 0 || Randomizer.nextInt(10) == 1) { //1/10
                                    nEquip.setJump((short) (nEquip.getJump() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getHp() > 0 || Randomizer.nextInt(5) == 1) { //1/5
                                    nEquip.setHp((short) (nEquip.getHp() + Randomizer.nextInt(5)));
                                }
                                if (nEquip.getMp() > 0 || Randomizer.nextInt(5) == 1) { //1/5
                                    nEquip.setMp((short) (nEquip.getMp() + Randomizer.nextInt(5)));
                                }
                                nEquip.setEnhance((byte) (nEquip.getEnhance() + 1));
                            }
                            break;
                        } else if (GameConstants.isPotentialScroll(scrollId.getItemId())) {
                            if (nEquip.getState() == 0) {
                                final int chanc = scrollId.getItemId() == 2_049_400 ? 90 : 70;
                                if (Randomizer.nextInt(100) > chanc) {
                                    return null; //destroyed, nib
                                }
                                nEquip.resetPotential();
                            }
                            break;
                        } else {
                            for (Entry<String, Integer> stat : stats.entrySet()) {
                                final String key = stat.getKey();

                            switch (key) {
                                case "STR":
                                    nEquip.setStr((short) (nEquip.getStr() + stat.getValue()));
                                    break;
                                case "DEX":
                                    nEquip.setDex((short) (nEquip.getDex() + stat.getValue()));
                                    break;
                                case "INT":
                                    nEquip.setInt((short) (nEquip.getInt() + stat.getValue()));
                                    break;
                                case "LUK":
                                    nEquip.setLuk((short) (nEquip.getLuk() + stat.getValue()));
                                    break;
                                case "PAD":
                                    nEquip.setWatk((short) (nEquip.getWatk() + stat.getValue()));
                                    break;
                                case "PDD":
                                    nEquip.setWdef((short) (nEquip.getWdef() + stat.getValue()));
                                    break;
                                case "MAD":
                                    nEquip.setMatk((short) (nEquip.getMatk() + stat.getValue()));
                                    break;
                                case "MDD":
                                    nEquip.setMdef((short) (nEquip.getMdef() + stat.getValue()));
                                    break;
                                case "ACC":
                                    nEquip.setAcc((short) (nEquip.getAcc() + stat.getValue()));
                                    break;
                                case "EVA":
                                    nEquip.setAvoid((short) (nEquip.getAvoid() + stat.getValue()));
                                    break;
                                case "Speed":
                                    nEquip.setSpeed((short) (nEquip.getSpeed() + stat.getValue()));
                                    break;
                                case "Jump":
                                    nEquip.setJump((short) (nEquip.getJump() + stat.getValue()));
                                    break;
                                case "MHP":
                                    nEquip.setHp((short) (nEquip.getHp() + stat.getValue()));
                                    break;
                                case "MMP":
                                    nEquip.setMp((short) (nEquip.getMp() + stat.getValue()));
                                    break;
                                case "MHPr":
                                    nEquip.setHpR((short) (nEquip.getHpR() + stat.getValue()));
                                    break;
                                case "MMPr":
                                    nEquip.setMpR((short) (nEquip.getMpR() + stat.getValue()));
                                    break;
                                default:
                                    break;
                            }
                            }
                            break;
                        }
                    }
                }
                if (!GameConstants.isCleanSlate(scrollId.getItemId()) && !GameConstants.isSpecialScroll(scrollId.getItemId()) && !GameConstants.isEquipScroll(scrollId.getItemId()) && !GameConstants.isPotentialScroll(scrollId.getItemId())) {
                    if (GameConstants.isAzwanScroll(scrollId.getItemId())) {
                        nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - stats.get("tuc")));
                    } else {
                        nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
                    }
                    nEquip.setLevel((byte) (nEquip.getLevel() + 1));
                }
            } else {
                if (!ws && !GameConstants.isCleanSlate(scrollId.getItemId()) && !GameConstants.isSpecialScroll(scrollId.getItemId()) && !GameConstants.isEquipScroll(scrollId.getItemId()) && !GameConstants.isPotentialScroll(scrollId.getItemId())) {
                    if (GameConstants.isAzwanScroll(scrollId.getItemId())) {
                        nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - stats.get("tuc")));
                    } else {
                        nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
                    }
                }
                if (Randomizer.nextInt(99) < curse) {
                    return null;
                }
            }
        }
        return equip;
    }

    /**
     *
     * @param equip
     * @param scrollId
     * @param chr
     * @return
     */
    public IItem scrollUpgradeItemEx(Item equip, Item scrollId, MapleCharacter chr) {
        if (equip.getType() != 1) {
            return equip;
        }
        Equip nEquip = (Equip) equip;
        int succe = getScrollSuccess(scrollId.getItemId()); //成功几率
        int curse = getScrollCursed(scrollId.getItemId()); //失败几率
        int craft = chr.getTrait(MapleTrait.MapleTraitType.craft).getLevel() / 10; //倾向系统的砸卷加
        int lucksKey = ItemFlag.幸运卷轴.check(equip.getFlag()) ? 10 : 0; //装备带有幸运卷轴的砸卷加成
        if (ItemFlag.幸运卷轴.check(equip.getFlag())) {
            equip.setFlag((byte) (equip.getFlag() - ItemFlag.幸运卷轴.getValue()));
        }
        int success = succe + craft + lucksKey;
        if (chr.isGM()) {
            chr.dropMessage(0x0B, "涅槃火焰 - 默认几率: " + succe + "% 倾向加成: " + craft + "% 幸运卷轴状态加成: " + lucksKey + "% 最终几率: " + success + "% 失败消失几率: " + curse + "%");
        }
        if (Randomizer.nextInt(100) <= success) {
            //   NirvanaFlame.Companion.randomState(nEquip, scrollId);
            short flag = equip.getFlag();
            if (!ItemFlag.UNTRADEABLE.check(flag) && !isAccountShared(equip.getItemId())) {
                nEquip.setKarmaCount((short) 10);
                flag |= (short) ItemFlag.UNTRADEABLE.getValue();
            }
            equip.setFlag((byte) flag);
        }
        return nEquip;
    }

    /**
     *
     * @param equipId
     * @return
     */
    public final IItem getEquipById(final int equipId) {
        return getEquipById(equipId, -1);
    }

    /**
     *
     * @param equipId
     * @param ringId
     * @return
     */
    public final IItem getEquipById(final int equipId, final int ringId) {
        final Equip nEquip = new Equip(equipId, (byte) 0, ringId, (byte) 0);
        nEquip.setQuantity((short) 1);
        final Map<String, Integer> stats = getEquipStats(equipId);
        if (stats != null) {
            for (Entry<String, Integer> stat : stats.entrySet()) {
                final String key = stat.getKey();

                switch (key) {
                    case "STR":
                        nEquip.setStr((short) stat.getValue().intValue());
                        break;
                    case "DEX":
                        nEquip.setDex((short) stat.getValue().intValue());
                        break;
                    case "INT":
                        nEquip.setInt((short) stat.getValue().intValue());
                        break;
                    case "LUK":
                        nEquip.setLuk((short) stat.getValue().intValue());
                        break;
                    case "PAD":
                        nEquip.setWatk((short) stat.getValue().intValue());
                        break;
                    case "PDD":
                        nEquip.setWdef((short) stat.getValue().intValue());
                        break;
                    case "MAD":
                        nEquip.setMatk((short) stat.getValue().intValue());
                        break;
                    case "MDD":
                        nEquip.setMdef((short) stat.getValue().intValue());
                        break;
                    case "ACC":
                        nEquip.setAcc((short) stat.getValue().intValue());
                        break;
                    case "EVA":
                        nEquip.setAvoid((short) stat.getValue().intValue());
                        break;
                    case "Speed":
                        nEquip.setSpeed((short) stat.getValue().intValue());
                        break;
                    case "Jump":
                        nEquip.setJump((short) stat.getValue().intValue());
                        break;
                    case "MHP":
                        nEquip.setHp((short) stat.getValue().intValue());
                        break;
                    case "MMP":
                        nEquip.setMp((short) stat.getValue().intValue());
                        break;
                    case "MHPr":
                        nEquip.setHpR((short) stat.getValue().intValue());
                        break;
                    case "MMPr":
                        nEquip.setMpR((short) stat.getValue().intValue());
                        break;
                    case "tuc":
                        nEquip.setUpgradeSlots(stat.getValue().byteValue());
                        break;
                    case "Craft":
                        nEquip.setHands(stat.getValue().shortValue());
                        break;
                    case "durability":
                        nEquip.setDurability(stat.getValue());
//                } else if (key.equals("afterImage")) {
                        break;
                    default:
                        break;
                }
            }
        }
        equipCache.put(equipId, nEquip);
        return nEquip.copy();
    }

    private short getRandStat(final short defaultValue, final int maxRange) {
        if (defaultValue == 0) {
            return 0;
        }
        // vary no more than ceil of 10% of stat
        final int lMaxRange = (int) Math.min(Math.ceil(defaultValue * 0.1), maxRange);

        return (short) ((defaultValue - lMaxRange) + Math.floor(Math.random() * (lMaxRange * 2 + 1)));
    }

    /**
     *
     * @param defaultValue
     * @param maxRange
     * @return
     */
    protected short getRandStatAbove(short defaultValue, int maxRange) {
        if (defaultValue <= 0) {
            return 0;
        }
        int lMaxRange = (int) Math.min(Math.ceil(defaultValue * 0.1D), maxRange);
        return (short) (defaultValue + Randomizer.nextInt(lMaxRange + 1));
    }

    /**
     *
     * @param defaultValue
     * @param value1
     * @param value2
     * @return
     */
    protected short getRandStatFusion(short defaultValue, int value1, int value2) {
        if (defaultValue == 0) {
            return 0;
        }
        int range = (value1 + value2) / 2 - defaultValue;
        int rand = Randomizer.nextInt(Math.abs(range) + 1);
        return (short) (defaultValue + (range < 0 ? -rand : rand));
    }

    /**
     *
     * @param equip
     * @return
     */
    public final Equip randomizeStats(final Equip equip) {
        equip.setStr(getRandStat(equip.getStr(), 5));
        equip.setDex(getRandStat(equip.getDex(), 5));
        equip.setInt(getRandStat(equip.getInt(), 5));
        equip.setLuk(getRandStat(equip.getLuk(), 5));
        equip.setMatk(getRandStat(equip.getMatk(), 5));
        equip.setWatk(getRandStat(equip.getWatk(), 5));
        equip.setAcc(getRandStat(equip.getAcc(), 5));
        equip.setAvoid(getRandStat(equip.getAvoid(), 5));
        equip.setJump(getRandStat(equip.getJump(), 5));
        equip.setHands(getRandStat(equip.getHands(), 5));
        equip.setSpeed(getRandStat(equip.getSpeed(), 5));
        equip.setWdef(getRandStat(equip.getWdef(), 10));
        equip.setMdef(getRandStat(equip.getMdef(), 10));
        equip.setHp(getRandStat(equip.getHp(), 10));
        equip.setMp(getRandStat(equip.getMp(), 10));
        return equip;
    }

    /**
     *
     * @param equip
     * @return
     */
    public Equip randomizeStats_Above(Equip equip) {
        equip.setStr(getRandStatAbove(equip.getStr(), 5));
        equip.setDex(getRandStatAbove(equip.getDex(), 5));
        equip.setInt(getRandStatAbove(equip.getInt(), 5));
        equip.setLuk(getRandStatAbove(equip.getLuk(), 5));
        equip.setMatk(getRandStatAbove(equip.getMatk(), 5));
        equip.setWatk(getRandStatAbove(equip.getWatk(), 5));
        equip.setAcc(getRandStatAbove(equip.getAcc(), 5));
        equip.setAvoid(getRandStatAbove(equip.getAvoid(), 5));
        equip.setJump(getRandStatAbove(equip.getJump(), 5));
        equip.setHands(getRandStatAbove(equip.getHands(), 5));
        equip.setSpeed(getRandStatAbove(equip.getSpeed(), 5));
        equip.setWdef(getRandStatAbove(equip.getWdef(), 10));
        equip.setMdef(getRandStatAbove(equip.getMdef(), 10));
        equip.setHp(getRandStatAbove(equip.getHp(), 10));
        equip.setMp(getRandStatAbove(equip.getMp(), 10));
        return equip;
    }

    /**
     *
     * @param equip1
     * @param equip2
     * @return
     */
    public Equip fuse(Equip equip1, Equip equip2) {
        if (equip1.getItemId() != equip2.getItemId()) {
            return equip1;
        }
        Equip equip = (Equip) getEquipById(equip1.getItemId());
        equip.setStr(getRandStatFusion(equip.getStr(), equip1.getStr(), equip2.getStr()));
        equip.setDex(getRandStatFusion(equip.getDex(), equip1.getDex(), equip2.getDex()));
        equip.setInt(getRandStatFusion(equip.getInt(), equip1.getInt(), equip2.getInt()));
        equip.setLuk(getRandStatFusion(equip.getLuk(), equip1.getLuk(), equip2.getLuk()));
        equip.setMatk(getRandStatFusion(equip.getMatk(), equip1.getMatk(), equip2.getMatk()));
        equip.setWatk(getRandStatFusion(equip.getWatk(), equip1.getWatk(), equip2.getWatk()));
        equip.setAcc(getRandStatFusion(equip.getAcc(), equip1.getAcc(), equip2.getAcc()));
        equip.setAvoid(getRandStatFusion(equip.getAvoid(), equip1.getAvoid(), equip2.getAvoid()));
        equip.setJump(getRandStatFusion(equip.getJump(), equip1.getJump(), equip2.getJump()));
        equip.setHands(getRandStatFusion(equip.getHands(), equip1.getHands(), equip2.getHands()));
        equip.setSpeed(getRandStatFusion(equip.getSpeed(), equip1.getSpeed(), equip2.getSpeed()));
        equip.setWdef(getRandStatFusion(equip.getWdef(), equip1.getWdef(), equip2.getWdef()));
        equip.setMdef(getRandStatFusion(equip.getMdef(), equip1.getMdef(), equip2.getMdef()));
        equip.setHp(getRandStatFusion(equip.getHp(), equip1.getHp(), equip2.getHp()));
        equip.setMp(getRandStatFusion(equip.getMp(), equip1.getMp(), equip2.getMp()));
        return equip;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final MapleStatEffect getItemEffect(final int itemId) {
        MapleStatEffect ret = itemEffects.get(itemId);
        if (ret == null) {
            final MapleData item = getItemData(itemId);
            if (item == null) {
                return null;
            }
            ret = MapleStatEffect.loadItemEffectFromData(item.getChildByPath("spec"), itemId);
            itemEffects.put(itemId, ret);
        }
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final List<Pair<Integer, Integer>> getSummonMobs(final int itemId) {
        if (summonMobCache.containsKey(itemId)) {
            return summonMobCache.get(itemId);
        }
        if (!GameConstants.isSummonSack(itemId)) {
            return null;
        }
        final MapleData data = getItemData(itemId).getChildByPath("mob");
        if (data == null) {
            return null;
        }
        final List<Pair<Integer, Integer>> mobPairs = new ArrayList<>();

        for (final MapleData child : data.getChildren()) {
            mobPairs.add(new Pair<>(
                    MapleDataTool.getIntConvert("id", child),
                    MapleDataTool.getIntConvert("prob", child)));
        }
        summonMobCache.put(itemId, mobPairs);
        return mobPairs;
    }

    /**
     *
     * @param id
     * @return
     */
    public final int getCardMobId(final int id) {
        if (id == 0) {
            return 0;
        }
        if (monsterBookID.containsKey(id)) {
            return monsterBookID.get(id);
        }
        final MapleData data = getItemData(id);
        final int monsterid = MapleDataTool.getIntConvert("info/mob", data, 0);

        if (monsterid == 0) { // Hack.
            return 0;
        }
        monsterBookID.put(id, monsterid);
        return monsterBookID.get(id);
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final int getWatkForProjectile(final int itemId) {
        Integer atk = projectileWatkCache.get(itemId);
        if (atk != null) {
            return atk;
        }
        final MapleData data = getItemData(itemId);
        atk = MapleDataTool.getInt("info/incPAD", data, 0);
        projectileWatkCache.put(itemId, atk);
        return atk;
    }

    /**
     *
     * @param scrollid
     * @param itemid
     * @return
     */
    public final boolean canScroll(final int scrollid, final int itemid) {
        return (scrollid / 100) % 100 == (itemid / 10_000) % 100;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final String getName(final int itemId) {
        if (nameCache.containsKey(itemId)) {
            return nameCache.get(itemId);
        }
        final MapleData strings = getStringData(itemId);
        if (strings == null) {
            return null;
        }
        final String ret = MapleDataTool.getString("name", strings, "(null)");
        nameCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final String getDesc(final int itemId) {
        if (descCache.containsKey(itemId)) {
            return descCache.get(itemId);
        }
        final MapleData strings = getStringData(itemId);
        if (strings == null) {
            return null;
        }
        final String ret = MapleDataTool.getString("desc", strings, null);
        descCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final String getMsg(final int itemId) {
        if (msgCache.containsKey(itemId)) {
            return msgCache.get(itemId);
        }
        final MapleData strings = getStringData(itemId);
        if (strings == null) {
            return null;
        }
        final String ret = MapleDataTool.getString("msg", strings, null);
        msgCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final short getItemMakeLevel(final int itemId) {
        if (itemMakeLevel.containsKey(itemId)) {
            return itemMakeLevel.get(itemId);
        }
        if (itemId / 10_000 != 400) {
            return 0;
        }
        final short lvl = (short) MapleDataTool.getIntConvert("info/lv", getItemData(itemId), 0);
        itemMakeLevel.put(itemId, lvl);
        return lvl;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final byte isConsumeOnPickup(final int itemId) {
        // 0 = not, 1 = consume on pickup, 2 = consume + party
        if (consumeOnPickupCache.containsKey(itemId)) {
            return consumeOnPickupCache.get(itemId);
        }
        final MapleData data = getItemData(itemId);
        byte consume = (byte) MapleDataTool.getIntConvert("spec/consumeOnPickup", data, 0);
        if (consume == 0) {
            consume = (byte) MapleDataTool.getIntConvert("specEx/consumeOnPickup", data, 0);
        }
        if (consume == 1) {
            if (MapleDataTool.getIntConvert("spec/party", getItemData(itemId), 0) > 0) {
                consume = 2;
            }
        }
        consumeOnPickupCache.put(itemId, consume);
        return consume;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean isDropRestricted(final int itemId) {
        if (dropRestrictionCache.containsKey(itemId)) {
            return dropRestrictionCache.get(itemId);
        }
        final MapleData data = getItemData(itemId);

        boolean trade = false;
        if (MapleDataTool.getIntConvert("info/tradeBlock", data, 0) == 1 || MapleDataTool.getIntConvert("info/quest", data, 0) == 1) {
            trade = true;
        }
        dropRestrictionCache.put(itemId, trade);
        return trade;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean isPickupRestricted(final int itemId) {
        if (pickupRestrictionCache.containsKey(itemId)) {
            return pickupRestrictionCache.get(itemId);
        }
        final boolean bRestricted = MapleDataTool.getIntConvert("info/only", getItemData(itemId), 0) == 1;

        pickupRestrictionCache.put(itemId, bRestricted);
        return bRestricted;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean isAccountShared(final int itemId) {
        if (accCache.containsKey(itemId)) {
            return accCache.get(itemId);
        }
        final boolean bRestricted = MapleDataTool.getIntConvert("info/accountSharable", getItemData(itemId), 0) == 1;

        accCache.put(itemId, bRestricted);
        return bRestricted;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final int getStateChangeItem(final int itemId) {
        if (stateChangeCache.containsKey(itemId)) {
            return stateChangeCache.get(itemId);
        }
        final int triggerItem = MapleDataTool.getIntConvert("info/stateChangeItem", getItemData(itemId), 0);
        stateChangeCache.put(itemId, triggerItem);
        return triggerItem;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final int getMeso(final int itemId) {
        if (mesoCache.containsKey(itemId)) {
            return mesoCache.get(itemId);
        }
        final int triggerItem = MapleDataTool.getIntConvert("info/meso", getItemData(itemId), 0);
        mesoCache.put(itemId, triggerItem);
        return triggerItem;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean isKarmaEnabled(final int itemId) {
        if (karmaEnabledCache.containsKey(itemId)) {
            return karmaEnabledCache.get(itemId) == 1;
        }
        final int iRestricted = MapleDataTool.getIntConvert("info/tradeAvailable", getItemData(itemId), 0);

        karmaEnabledCache.put(itemId, iRestricted);
        return iRestricted == 1;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean isPKarmaEnabled(final int itemId) {
        if (karmaEnabledCache.containsKey(itemId)) {
            return karmaEnabledCache.get(itemId) == 2;
        }
        final int iRestricted = MapleDataTool.getIntConvert("info/tradeAvailable", getItemData(itemId), 0);

        karmaEnabledCache.put(itemId, iRestricted);
        return iRestricted == 2;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean isPickupBlocked(final int itemId) {
        if (blockPickupCache.containsKey(itemId)) {
            return blockPickupCache.get(itemId);
        }
        final boolean iRestricted = MapleDataTool.getIntConvert("info/pickUpBlock", getItemData(itemId), 0) == 1;

        blockPickupCache.put(itemId, iRestricted);
        return iRestricted;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean isLogoutExpire(final int itemId) {
        if (logoutExpireCache.containsKey(itemId)) {
            return logoutExpireCache.get(itemId);
        }
        final boolean iRestricted = MapleDataTool.getIntConvert("info/expireOnLogout", getItemData(itemId), 0) == 1;

        logoutExpireCache.put(itemId, iRestricted);
        return iRestricted;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean cantSell(final int itemId) { //true = cant sell, false = can sell
        if (notSaleCache.containsKey(itemId)) {
            return notSaleCache.get(itemId);
        }
        final boolean bRestricted = MapleDataTool.getIntConvert("info/notSale", getItemData(itemId), 0) == 1;

        notSaleCache.put(itemId, bRestricted);
        return bRestricted;
    }

    /**
     *
     * @param itemid
     * @return
     */
    public final Pair<Integer, List<StructRewardItem>> getRewardItem(final int itemid) {
        if (RewardItem.containsKey(itemid)) {
            return RewardItem.get(itemid);
        }
        final MapleData data = getItemData(itemid);
        if (data == null) {
            return null;
        }
        final MapleData rewards = data.getChildByPath("reward");
        if (rewards == null) {
            return null;
        }
        int totalprob = 0; // As there are some rewards with prob above 2000, we can't assume it's always 100
        List<StructRewardItem> all = new ArrayList<>();

        for (final MapleData reward : rewards) {
            StructRewardItem struct = new StructRewardItem();

            struct.itemid = MapleDataTool.getInt("item", reward, 0);
            struct.prob = (byte) MapleDataTool.getInt("prob", reward, 0);
            struct.quantity = (short) MapleDataTool.getInt("count", reward, 0);
            struct.effect = MapleDataTool.getString("Effect", reward, "");
            struct.worldmsg = MapleDataTool.getString("worldMsg", reward, null);
            struct.period = MapleDataTool.getInt("period", reward, -1);

            totalprob += struct.prob;

            all.add(struct);
        }
        Pair<Integer, List<StructRewardItem>> toreturn = new Pair<>(totalprob, all);
        RewardItem.put(itemid, toreturn);
        return toreturn;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final Map<String, Integer> getSkillStats(final int itemId) { //物品技能
        if (SkillStatsCache.containsKey(itemId)) {
            return SkillStatsCache.get(itemId);
        }
        if (!(itemId / 10_000 == 228 || itemId / 10_000 == 229 || itemId / 10_000 == 562)) { // Skillbook and mastery book
            return null;
        }
        final MapleData item = getItemData(itemId);
        if (item == null) {
            return null;
        }
        final MapleData info = item.getChildByPath("info");
        if (info == null) {
            return null;
        }
        final Map<String, Integer> ret = new LinkedHashMap<>();
        for (final MapleData data : info.getChildren()) {
            if (data.getName().startsWith("inc")) {
                ret.put(data.getName().substring(3), MapleDataTool.getIntConvert(data));
            }
        }
        ret.put("masterLevel", MapleDataTool.getInt("masterLevel", info, 0));
        ret.put("reqSkillLevel", MapleDataTool.getInt("reqSkillLevel", info, 0));
        ret.put("success", MapleDataTool.getInt("success", info, 0));

        final MapleData skill = info.getChildByPath("skill");

        for (int i = 0; i < skill.getChildren().size(); i++) { // List of allowed skillIds
            ret.put("skillid" + i, MapleDataTool.getInt(Integer.toString(i), skill, 0));
        }
        SkillStatsCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final List<Integer> petsCanConsume(final int itemId) {
        if (petsCanConsumeCache.get(itemId) != null) {
            return petsCanConsumeCache.get(itemId);
        }
        final List<Integer> ret = new ArrayList<>();
        final MapleData data = getItemData(itemId);
        if (data == null || data.getChildByPath("spec") == null) {
            return ret;
        }
        int curPetId = 0;
        for (MapleData c : data.getChildByPath("spec")) {
            try {
                Integer.parseInt(c.getName());
            } catch (NumberFormatException e) {
                continue;
            }
            curPetId = MapleDataTool.getInt(c, 0);
            if (curPetId == 0) {
                break;
            }
            ret.add(curPetId);
        }
        petsCanConsumeCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean isQuestItem(final int itemId) {
        if (isQuestItemCache.containsKey(itemId)) {
            return isQuestItemCache.get(itemId);
        }
        final boolean questItem = MapleDataTool.getIntConvert("info/quest", getItemData(itemId), 0) == 1;
        isQuestItemCache.put(itemId, questItem);
        return questItem;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final Pair<Integer, List<Integer>> questItemInfo(final int itemId) {
        if (questItems.containsKey(itemId)) {
            return questItems.get(itemId);
        }
        if (itemId / 10_000 != 422 || getItemData(itemId) == null) {
            return null;
        }
        final MapleData itemD = getItemData(itemId).getChildByPath("info");
        if (itemD == null || itemD.getChildByPath("consumeItem") == null) {
            return null;
        }
        final List<Integer> consumeItems = new ArrayList<>();
        for (MapleData consume : itemD.getChildByPath("consumeItem")) {
            consumeItems.add(MapleDataTool.getInt(consume, 0));
        }
        final Pair<Integer, List<Integer>> questItem = new Pair<>(MapleDataTool.getIntConvert("questId", itemD, 0), consumeItems);
        questItems.put(itemId, questItem);
        return questItem;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean itemExists(final int itemId) {
        if (GameConstants.getInventoryType(itemId) == MapleInventoryType.UNDEFINED) {
            return false;
        }
        return getItemData(itemId) != null;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public final boolean isCash(final int itemId) {
        if (getEquipStats(itemId) == null) {
            return GameConstants.getInventoryType(itemId) == MapleInventoryType.CASH;
        }
        return GameConstants.getInventoryType(itemId) == MapleInventoryType.CASH || getEquipStats(itemId).get("cash") > 0;

    }

    /**
     *
     * @param itemId
     * @return
     */
    public MapleInventoryType getInventoryType(int itemId) {
        if (inventoryTypeCache.containsKey(itemId)) {
            return inventoryTypeCache.get(itemId);
        }
        MapleInventoryType ret;
        String idStr = "0" + String.valueOf(itemId);
        MapleDataDirectoryEntry root = itemData.getRoot();
        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            for (MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr.substring(0, 4) + ".img")) {
                    ret = MapleInventoryType.getByWZName(topDir.getName());
                    inventoryTypeCache.put(itemId, ret);
                    return ret;
                } else if (iFile.getName().equals(idStr.substring(1) + ".img")) {
                    ret = MapleInventoryType.getByWZName(topDir.getName());
                    inventoryTypeCache.put(itemId, ret);
                    return ret;
                }
            }
        }
        root = equipData.getRoot();
        for (MapleDataDirectoryEntry topDir : root.getSubdirectories()) {
            for (MapleDataFileEntry iFile : topDir.getFiles()) {
                if (iFile.getName().equals(idStr + ".img")) {
                    ret = MapleInventoryType.EQUIP;
                    inventoryTypeCache.put(itemId, ret);
                    return ret;
                }
            }
        }
        ret = MapleInventoryType.UNDEFINED;
        inventoryTypeCache.put(itemId, ret);
        return ret;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public short getPetFlagInfo(int itemId) {
        short flag = 0;
        if (itemId / 10_000 != 500) {
            return flag;
        }
        MapleData item = getItemData(itemId);
        if (item == null) {
            return flag;
        }
        if (MapleDataTool.getIntConvert("info/pickupItem", item, 0) > 0) {
            flag = (short) (flag | 0x1);
        }
        if (MapleDataTool.getIntConvert("info/longRange", item, 0) > 0) {
            flag = (short) (flag | 0x2);
        }
        if (MapleDataTool.getIntConvert("info/pickupAll", item, 0) > 0) {
            flag = (short) (flag | 0x4);
        }
        if (MapleDataTool.getIntConvert("info/sweepForDrop", item, 0) > 0) {
            flag = (short) (flag | 0x10);
        }
        if (MapleDataTool.getIntConvert("info/consumeHP", item, 0) > 0) {
            flag = (short) (flag | 0x20);
        }
        if (MapleDataTool.getIntConvert("info/consumeMP", item, 0) > 0) {
            flag = (short) (flag | 0x40);
        }
        //this.petFlagInfo.put(Integer.valueOf(itemId), Short.valueOf(flag));
        return flag;
    }

    /**
     * 宠物触发的套装ID
     */
    public int getPetSetItemID(int itemId) {
        if (itemId / 10_000 != 500) {
            return -1;
        }
        return getEquipStats(itemId).get("info/setItemID");
    }

    /**
     * 装备加百分百HP
     *
     * @param itemId
     */
    public int getItemIncMHPr(int itemId) {
        return getEquipStats(itemId).get("info/MHPr");
    }

    /**
     * 装备加百分百MP
     *
     * @param itemId
     */
    public int getItemIncMMPr(int itemId) {
        return getEquipStats(itemId).get("info/MMPr");
    }

    /**
     * 卷轴成功几率 几个特殊的卷 2046006 - 周年庆单手武器攻击力卷轴 - 提高单手武器的功能物理攻击力属性。
     *
     * @param itemId
     * @return
     */
    public int getSuccessRates(int itemId) {
        if ((itemId / 10_000) != 204) {
            return 0;
        }
        return getEquipStats(itemId).get("info/successRates/0");
    }

    /**
     * 强化卷轴成功提升的星级
     */
    public int getForceUpgrade(int itemId) {
        if (itemId / 100 != 20_493) {
            return 0;
        }
        return getEquipStats(itemId).get("info/forceUpgrade");
    }

    /**
     * 卷轴失败不装备不损坏的卷轴
     */
    public boolean isNoCursedScroll(int itemId) {
        return itemId / 10_000 == 204 && getEquipStats(itemId).get("info/noCursed") == 1;
    }

    /**
     * 正向卷轴 不减少道具属性
     */
    public boolean isNegativeScroll(int itemId) {
        return itemId / 10_000 == 204 && getEquipStats(itemId).get("info/noNegative") == 1;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public int getRecover(int itemId) {
        return getEquipStats(itemId).get("info/recover");
    }

    /**
     *
     * @param itemId
     * @return
     */
    public boolean isKarmaAble(int itemId) {
        if (this.karmaCache.containsKey(itemId)) {
            return (this.karmaCache.get(itemId));
        }
        MapleData data = getItemData(itemId);
        boolean bRestricted = MapleDataTool.getIntConvert("info/tradeAvailable", data, 0) > 0; //可以交易
        this.karmaCache.put(itemId, bRestricted);
        return bRestricted;
    }

    /**
     *
     * @param itemId
     * @param level
     * @param timeless
     * @return
     */
    public List<Pair<String, Integer>> getItemLevelupStats(int itemId, int level, boolean timeless) {
        //timeless 永恒
        List<Pair<String, Integer>> list = new LinkedList<>();
        MapleData data = getItemData(itemId); //获得该物品所有节点
        MapleData data1 = data.getChildByPath("info").getChildByPath("level");
        /*
         * if ((timeless && level == 5) || (!timeless && level == 3)) {
         * MapleData skilldata =
         * data1.getChildByPath("case").getChildByPath("1").getChildByPath(timeless
         * ? "6" : "4"); if (skilldata != null) { int skillid; List<MapleData>
         * skills = skilldata.getChildByPath("Skill").getChildren(); for (int i
         * = 0; i < skills.size(); i++) { skillid =
         * MapleDataTool.getInt(skills.get(i).getChildByPath("id"));
         * //System.out.println(skillid); if (Math.random() < 0.1) list.add(new
         * Pair<String, Integer>("Skill" + i, skillid)); } } }
         */
        if (data1 != null) { //判断装备是否存在level节点
            MapleData data2 = data1.getChildByPath("info").getChildByPath(Integer.toString(level)); //获取与装备的[道具等级]相应的节点
            if (data2 != null) {
                for (MapleData da : data2.getChildren()) {
                    if (Math.random() < 0.9) {
                        if (da.getName().startsWith("incDEXMin")) {
                            list.add(new Pair<>("incDEX", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incDEXMax")))));
                        } else if (da.getName().startsWith("incSTRMin")) {
                            list.add(new Pair<>("incSTR", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incSTRMax")))));
                        } else if (da.getName().startsWith("incINTMin")) {
                            list.add(new Pair<>("incINT", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incINTMax")))));
                        } else if (da.getName().startsWith("incLUKMin")) {
                            list.add(new Pair<>("incLUK", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incLUKMax")))));
                        } else if (da.getName().startsWith("incMHPMin")) {
                            list.add(new Pair<>("incMHP", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incMHPMax")))));
                        } else if (da.getName().startsWith("incMMPMin")) {
                            list.add(new Pair<>("incMMP", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incMMPMax")))));
                        } else if (da.getName().startsWith("incPADMin")) {
                            list.add(new Pair<>("incPAD", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incPADMax")))));
                        } else if (da.getName().startsWith("incMADMin")) {
                            list.add(new Pair<>("incMAD", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incMADMax")))));
                        } else if (da.getName().startsWith("incPDDMin")) {
                            list.add(new Pair<>("incPDD", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incPDDMax")))));
                        } else if (da.getName().startsWith("incMDDMin")) {
                            list.add(new Pair<>("incMDD", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incMDDMax")))));
                        } else if (da.getName().startsWith("incACCMin")) {
                            list.add(new Pair<>("incACC", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incACCMax")))));
                        } else if (da.getName().startsWith("incEVAMin")) {
                            list.add(new Pair<>("incEVA", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incEVAMax")))));
                        } else if (da.getName().startsWith("incSpeedMin")) {
                            list.add(new Pair<>("incSpeed", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incSpeedMax")))));
                        } else if (da.getName().startsWith("incJumpMin")) {
                            list.add(new Pair<>("incJump", rand(MapleDataTool.getInt(da), MapleDataTool.getInt(data2.getChildByPath("incJumpMax")))));
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public boolean isUntradeableOnEquip(int itemId) {
        if (onEquipUntradableCache.containsKey(itemId)) {
            return onEquipUntradableCache.get(itemId);
        }
        boolean untradableOnEquip = MapleDataTool.getIntConvert("info/equipTradeBlock", getItemData(itemId), 0) > 0;
        onEquipUntradableCache.put(itemId, untradableOnEquip);
        return untradableOnEquip;
    }

    /**
     *
     * @param itemId
     * @return
     */
    public int getExpCache(int itemId) {
        if (getExpCache.containsKey(itemId)) {
            return (getExpCache.get(itemId));
        }
        MapleData item = getItemData(itemId);
        if (item == null) {
            return 0;
        }
        int pEntry = 0;
        MapleData pData = item.getChildByPath("spec/exp");
        if (pData == null) {
            return 0;
        }
        pEntry = MapleDataTool.getInt(pData);

        getExpCache.put(itemId, pEntry);
        return pEntry;
    }

    /**
     *
     * @param equip
     * @return
     */
    public int getTotalStat(Equip equip) {
        return equip.getStr() + equip.getDex() + equip.getInt() + equip.getLuk() + equip.getMatk() + equip.getWatk() + equip.getAcc() + equip.getAvoid() + equip.getJump() + equip.getHands() + equip.getSpeed() + equip.getHp() + equip.getMp() + equip.getWdef() + equip.getMdef();
    }
}
