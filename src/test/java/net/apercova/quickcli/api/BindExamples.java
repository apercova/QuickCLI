package net.percova.console.cli.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Date;

import net.apercova.quickcli.api.BaseCommand;
import net.apercova.quickcli.api.CLIArgument;
import net.apercova.quickcli.api.CLICommand;
import net.apercova.quickcli.api.CLIDatatypeConverter;
import net.apercova.quickcli.api.SimpleCharsetConverter;
import net.apercova.quickcli.api.SimpleDateConverter;

@CLICommand("bind-ex")
public class BindExamples extends BaseCommand{
	
		
	@CLIArgument(name="--texto",aliases={"--string"}, required=true)
	private String texto;
	@CLIArgument(name="--bits",aliases={"--byte"}, required=true)
	private byte bits;
	@CLIArgument(name="--corto",aliases={"--short"}, required=true)
	private short corto;
	@CLIArgument(name="--booleano",aliases={"--boolean"}, value="true")
	private boolean booleano;
	@CLIArgument(name="--entero",aliases={"--int"}, value="-1")
	private int entero;
	@CLIArgument(name="--largo",aliases={"--long"}, required=true)
	private long largo;
	@CLIArgument(name="--flotante",aliases={"--float"}, required=true)
	private float flotante;
	@CLIArgument(name="--doble",aliases={"--double"}, required=true)
	private double doble;
	@CLIArgument(name="--bint",aliases={"--big-integer"}, required=true)
	private BigInteger bint;
	@CLIArgument(name="--bdec",aliases={"--big-decimal"}, required=true)
	private BigDecimal bdec;
	
	@CLIArgument(name="--date",required=true)
	@CLIDatatypeConverter(SimpleDateConverter.class)
	private Date date;
	
	@CLIArgument(name="--cs", value="us-ascii")
	@CLIDatatypeConverter(SimpleCharsetConverter.class)
	private Charset charset;
	
	@CLIArgument(name="--help", usage="List available options" )	
	private Boolean showHelp;

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public byte getBits() {
		return bits;
	}

	public void setBits(byte bits) {
		this.bits = bits;
	}

	public short getCorto() {
		return corto;
	}

	public void setCorto(short corto) {
		this.corto = corto;
	}

	public boolean isBooleano() {
		return booleano;
	}

	public void setBooleano(boolean booleano) {
		this.booleano = booleano;
	}

	public int getEntero() {
		return entero;
	}

	public void setEntero(int entero) {
		this.entero = entero;
	}

	public long getLargo() {
		return largo;
	}

	public void setLargo(long largo) {
		this.largo = largo;
	}

	public float getFlotante() {
		return flotante;
	}

	public void setFlotante(float flotante) {
		this.flotante = flotante;
	}

	public double getDoble() {
		return doble;
	}

	public void setDoble(double doble) {
		this.doble = doble;
	}

	public BigInteger getBint() {
		return bint;
	}

	public void setBint(BigInteger bint) {
		this.bint = bint;
	}

	public BigDecimal getBdec() {
		return bdec;
	}

	public void setBdec(BigDecimal bdec) {
		this.bdec = bdec;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	@Override
	public String toString() {
		return "BindExamples [texto=" + texto + ", bits=" + bits + ", corto=" + corto + ", booleano=" + booleano
				+ ", entero=" + entero + ", largo=" + largo + ", flotante=" + flotante + ", doble=" + doble + ", bint="
				+ bint + ", bdec=" + bdec + ", date=" + date + ", charset=" + charset + "]";
	}
	
}
