package ru.vetatto.investing.investing.PifInfo;

public class PifStructureData {
    String emittet;
    String procent;


    public PifStructureData(String emittet, String procent) {
        this.emittet = emittet;
        this.procent =procent;
    }

    public String getPifStructureEmittet(){ return emittet; }
    public String getPifStructureProcent(){return procent;}
}
