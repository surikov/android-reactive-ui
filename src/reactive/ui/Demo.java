package reactive.ui;

//http://www3.ntu.edu.sg/home/ehchua/programming/android/Android_3D.html
import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.*;
import android.net.*;
import android.widget.*;

import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.params.*;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.database.*;
import android.database.sqlite.*;
import reactive.ui.*;

import java.net.*;
import java.nio.channels.FileChannel;

import android.view.animation.*;
import android.view.inputmethod.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import java.io.*;
import java.text.*;

public class Demo extends Activity {
	Layoutless layoutless;
	Grid testGrid;
	Bough testData;
	static SQLiteDatabase cacheSQLiteDatabase = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layoutless = new Layoutless(this);
		
		this.setContentView(layoutless);
		layoutless//
		.child(new Decor(this)//
		.background.is(0x33ff0000).width().is(200).height().is(200)//
		)//
		.child(new Decor(this)//
		.background.is(0x3300ff00).width().is(200).height().is(200).left().is(150).top().is(50)//
		)//
;
		System.out.println("go----------------------------------");
		//setupStrippedData(db());
		//db().execSQL("analyze");
		//System.out.println("old11111111111111111111111111111111111111111111");
		//testReadOld();
		//System.out.println("new222222222222222222222222222222222222222222");
		
		new CannyTask(){

			@Override
			public void doTask() {
				testReadNew();
				addTestGrid();
				
				
			}}.start(200);
		
		/*CannyTask t=new CannyTask(){

			@Override
			public void doTask() {
				System.out.println("go!");
			}};
			t.start();
			t.start();
			t.start();
			t.start();*/
		System.out.println("done onCreate");
	}
	void addTestGrid() {
		
		testGrid=new Grid(this);
		layoutless.child(testGrid//
				.width().is(900).height().is(500).left().is(100).top().is(100)//
				)//
		;
		GridColumnText colArtikul= new GridColumnText();
		GridColumnText colName= new GridColumnText();
		GridColumnText colProizvoditel= new GridColumnText();
		GridColumnText colMinKol= new GridColumnText();
		GridColumnText colKolMest= new GridColumnText();
		GridColumnText colEdizm= new GridColumnText();
		GridColumnText colCena= new GridColumnText();
		GridColumnText colRazmSkidki= new GridColumnText();
		GridColumnText colVidSkidki= new GridColumnText();
		GridColumnText colPoslCena= new GridColumnText();
		GridColumnText colMinCena= new GridColumnText();
		GridColumnText colMaxCena= new GridColumnText();
		GridColumnText colPhoto= new GridColumnText();
		System.out.println("fill columns");
		for(int i=0;i<testData.children.size();i++){
			Bough row=testData.children.get(i);
			colArtikul.cell(row.child("Artikul").value.property.value());
			colName.cell(row.child("Naimenovanie").value.property.value());
			colProizvoditel.cell(row.child("ProizvoditelNaimenovanie").value.property.value());
			colMinKol.cell(row.child("1").value.property.value());
			colKolMest.cell(row.child("2").value.property.value());
			colEdizm.cell(row.child("st").value.property.value());
			colCena.cell(row.child("BasePrice").value.property.value());
			colRazmSkidki.cell(row.child("nope").value.property.value());
			colVidSkidki.cell(row.child("emp").value.property.value());
			colPoslCena.cell(row.child("?").value.property.value());
			colMinCena.cell(row.child("MinCena").value.property.value());
			colMaxCena.cell(row.child("MaxCena").value.property.value());
			colPhoto.cell(row.child("---").value.property.value());
		}
		System.out.println("fill grid");
		testGrid.setData(new GridColumn[] {colArtikul.width.is(60).title.is("Артикул")//
				,colName.width.is(250).title.is("Наименование")//
				,colProizvoditel.width.is(100).title.is("Производитель")//
				,colMinKol.width.is(40).title.is("Мин. кол.")//
				,colKolMest.width.is(40).title.is("Кол. мест")//
				,colEdizm.width.is(40).title.is("Ед. изм.")//
				,colCena.width.is(70).title.is("Цена")//
				,colRazmSkidki.width.is(60).title.is("Разм. скидки")//
				,colVidSkidki.width.is(100).title.is("Вид скидки")//
				,colPoslCena.width.is(70).title.is("Посл. цена")//
				,colMinCena.width.is(70).title.is("Мин. цена")//
				,colMaxCena.width.is(70).title.is("Макс. цена")//
				,colPhoto.width.is(Layoutless.tapSize).title.is("Фото")//
				});
		System.out.println("done add columns");
		testGrid.flip();
		//testGrid.data(new GridColumn[] {c1,c2});
		System.out.println("done fill columns");
		
	}
	void testReadOld() {
		System.out.println("testReadOld");
		String sql = " select n._id "//					
				//+ "\n 	,n.[_IDRRef] "//					
				//+ "\n 	,n.[Artikul] "//					
				+ "\n 	,n.[Naimenovanie] "//	
				/*
				+ "\n 	,n.[OsnovnoyProizvoditel] "//					
				+ "\n 	,p.Naimenovanie as ProizvoditelNaimenovanie "//
				+ "\n 	,CenyNomenklaturySklada.Cena as Cena "//
				+ "\n 	,0 as Skidka "//					
				+ "\n 	,0 as CenaSoSkidkoy "//					
				+ "\n 	,x'00' as VidSkidki "//	
				+ "\n 	,eho.[Naimenovanie] as [EdinicyIzmereniyaNaimenovanie] "//
				+ "\n 	,VelichinaKvantovNomenklatury.Kolichestvo as MinNorma "//
				+ "\n 	,ei.Koephphicient as [Koephphicient] "//					
				+ "\n  	,ei._IDRRef as [EdinicyIzmereniyaID] "//					
				+ "\n  	,n.Roditel as Roditel "//
				+ "\n 	,(1.0+ifnull(n1.nacenka,ifnull(n2.nacenka,ifnull(n3.nacenka,ifnull(n4.nacenka,n5.nacenka))))/100.0)*TekuschieCenyOstatkovPartiy.Cena as MinCena "//
				+ "\n 	,(1.0+const.MaksNacenkaCenyPraysa/100.0)*CenyNomenklaturySklada.Cena as MaxCena "//
				+ "\n 	,TekuschieCenyOstatkovPartiy.cena as BasePrice "//
				+ "\n 	,Prodazhi.Stoimost/Prodazhi.kolichestvo as LastPrice "//
				+ "\n 	,ifnull(nk1.ProcentSkidkiNacenki,nk2.ProcentSkidkiNacenki) as Nacenka "//
				+ "\n 	,ZapretSkidokTov.Individualnye as ZapretSkidokTovIndividualnye "//
				+ "\n 	,ZapretSkidokTov.Nokopitelnye as ZapretSkidokTovNokopitelnye "//
				+ "\n 	,ZapretSkidokTov.Partner as ZapretSkidokTovPartner "//
				+ "\n 	,ZapretSkidokTov.Razovie as ZapretSkidokTovRazovie "//
				+ "\n 	,ZapretSkidokTov.Nacenki as ZapretSkidokTovNacenki "//
				+ "\n 	,ZapretSkidokProizv.Individualnye as ZapretSkidokProizvIndividualnye "//
				+ "\n 	,ZapretSkidokProizv.Nokopitelnye as ZapretSkidokProizvNokopitelnye "//
				+ "\n 	,ZapretSkidokProizv.Partner as ZapretSkidokProizvPartner "//
				+ "\n 	,ZapretSkidokProizv.Razovie as ZapretSkidokProizvRazovie "//
				+ "\n 	,ZapretSkidokProizv.Nacenki as ZapretSkidokProizvNacenki "//
				+ "\n 	,ifnull(fx1.FixCena,fx2.FixCena) as FiksirovannyeCeny "//
				+ "\n 	,SkidkaPartneraKarta.ProcentSkidkiNacenki as SkidkaPartneraKarta "//
				+ "\n 	,NakopitelnyeSkidki.ProcentSkidkiNacenki as NakopitelnyeSkidki "//
				+ "\n 	,Prodazhi.period as LastSell "// 
				+ "\n 	,skladKazan.sklad as skladKazan "//
				+ "\n 	,skladHoreca.sklad as skladHoreca "//
				*/
				+ "\n  	from Nomenklatura n "//	
				+ "\n 	join Consts const "// 
				+ "\n 	join (select "// 
				+ "\n 			'2013-01-30' as dataOtgruzki "//
				+ "\n 			,x'935B18A90562E07411E1BC64CF4E1505' as kontragent "//
				+ "\n 			,x'B443002264FA89D811E02D3AF097290A' as polzovatel "//
				+ "\n 		) parameters "//		
				+ "\n 	join TekuschieCenyOstatkovPartiy on TekuschieCenyOstatkovPartiy.nomenklatura=n.[_IDRRef] "//
				+ "\n 	join Polzovateli on Polzovateli._idrref=parameters.polzovatel "//
				+ "\n 	join Podrazdeleniya p1 on p1._idrref=Polzovateli.podrazdelenie "//
				+ "\n 	join EdinicyIzmereniya eho on n.EdinicaKhraneniyaOstatkov = eho._IDRRef "//			
				+ "\n 	join kontragenty on kontragenty._idrref=parameters.kontragent "//		
				+ "\n 	join EdinicyIzmereniya ei on n.EdinicaDlyaOtchetov = ei._IDRRef "//	
				+ "\n 	join CenyNomenklaturySklada on CenyNomenklaturySklada.nomenklatura=n.[_IDRRef] "// 
				+ "\n 		and CenyNomenklaturySklada.Period=(select max(Period) from CenyNomenklaturySklada "//			
				+ "\n 				where nomenklatura=n.[_IDRRef] "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "//
				+ "\n 				) "//	
				+ "\n 	left join NacenkiKontr nk1 on nk1.PoluchatelSkidki=parameters.kontragent "// 
				+ "\n 			and nk1.Period=(select max(Period) from NacenkiKontr "// 
				+ "\n 				where PoluchatelSkidki=parameters.kontragent "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "// "//
				+ "\n 				) "//
				+ "\n 	left join NacenkiKontr nk2 on nk2.PoluchatelSkidki=kontragenty.GolovnoyKontragent "// 
				+ "\n 			and nk2.Period=(select max(Period) from NacenkiKontr "// 
				+ "\n 				where PoluchatelSkidki=kontragenty.GolovnoyKontragent "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "//
				+ "\n 					) "//
				+ "\n 	left join ZapretSkidokTov on ZapretSkidokTov.Nomenklatura=n.[_IDRRef] "// 
				+ "\n 			and ZapretSkidokTov.Period=(select max(Period) from ZapretSkidokTov "// 
				+ "\n 				where Nomenklatura=n.[_IDRRef] "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "//
				+ "\n 				) "//
				+ "\n 	left join ZapretSkidokProizv on ZapretSkidokProizv.Proizvoditel=n.[OsnovnoyProizvoditel] "//					
				+ "\n 				and ZapretSkidokProizv.Period=(select max(Period) from ZapretSkidokProizv "//		
				+ "\n 					where Proizvoditel=n.[OsnovnoyProizvoditel] "// 
				+ "\n 						and date(period)<=date(parameters.dataOtgruzki) "//
				+ "\n 					) "//
				+ "\n 	left join Podrazdeleniya p2  on p1.roditel=p2._idrref "//
				+ "\n 	left join Podrazdeleniya p3  on p2.roditel=p3._idrref "//
				+ "\n 	left join Podrazdeleniya p4  on p3.roditel=p4._idrref "//
				+ "\n 	left join MinimalnyeNacenkiProizvoditeley_1 n1  on n1.podrazdelenie=p1._idrref "// 
				+ "\n 				and n1.NomenklaturaProizvoditel_2=n._idrref "// 
				+ "\n 				and n1.period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 "// 
				+ "\n 					where podrazdelenie=p1._idrref "// 
				+ "\n 					and NomenklaturaProizvoditel_2=n._idrref "//
				+ "\n 					) "//
				+ "\n 	left join MinimalnyeNacenkiProizvoditeley_1 n2  on n2.podrazdelenie=p2._idrref "// 
				+ "\n 				and n2.NomenklaturaProizvoditel_2=n._idrref "// 
				+ "\n 				and n2.period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 "// 
				+ "\n 					where podrazdelenie=p2._idrref "// 
				+ "\n 					and NomenklaturaProizvoditel_2=n._idrref "//
				+ "\n 					) "//
				+ "\n 	left join MinimalnyeNacenkiProizvoditeley_1 n3  on n3.podrazdelenie=p3._idrref "// 
				+ "\n 				and n3.NomenklaturaProizvoditel_2=n._idrref "// 
				+ "\n 				and n3.period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 "// 
				+ "\n 					where podrazdelenie=p3._idrref "// 
				+ "\n 					and NomenklaturaProizvoditel_2=n._idrref "//
				+ "\n 					) "//
				+ "\n 	left join MinimalnyeNacenkiProizvoditeley_1 n4  on n4.podrazdelenie=p4._idrref "// 
				+ "\n 				and n4.NomenklaturaProizvoditel_2=n._idrref "// 
				+ "\n 				and n4.period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 "// 
				+ "\n 					where podrazdelenie=p4._idrref "// 
				+ "\n 					and NomenklaturaProizvoditel_2=n._idrref "//
				+ "\n 					) "//
				+ "\n 	left join MinimalnyeNacenkiProizvoditeley_1 n5  on n5.podrazdelenie=X'00000000000000000000000000000000' "// 
				+ "\n 				and n5.NomenklaturaProizvoditel_2=n._idrref "// 
				+ "\n 				and n5.period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 "// 
				+ "\n 					where podrazdelenie=X'00000000000000000000000000000000' "// 
				+ "\n 					and NomenklaturaProizvoditel_2=n._idrref "//
				+ "\n 					) "//	
				+ "\n 	left join Proizvoditel p on n.[OsnovnoyProizvoditel] = p._IDRRef "//
				+ "\n 	left join VelichinaKvantovNomenklatury on VelichinaKvantovNomenklatury.nomenklatura=n.[_IDRRef] "// 
				+ "\n 	left join FiksirovannyeCeny fx1 on fx1.DataOkonchaniya=( "// 
				+ "\n 					select max(DataOkonchaniya) from FiksirovannyeCeny "// 
				+ "\n 						where PoluchatelSkidki=parameters.kontragent "// 
				+ "\n 						and date(period)<=date(parameters.dataOtgruzki) "// 
				+ "\n 						and date(DataOkonchaniya)>=date(parameters.dataOtgruzki) "// 
				+ "\n 						and Nomenklatura=n.[_IDRRef] "// 
				+ "\n 				) and fx1.PoluchatelSkidki=parameters.kontragent "// 
				+ "\n 				and fx1.Nomenklatura=n.[_IDRRef] "// 
				+ "\n 	left join FiksirovannyeCeny fx2 on fx2.DataOkonchaniya=( "// 
				+ "\n 					select max(DataOkonchaniya) from FiksirovannyeCeny "// 
				+ "\n 						where PoluchatelSkidki=kontragenty.GolovnoyKontragent "// 
				+ "\n 						and date(period)<=date(parameters.dataOtgruzki) "// 
				+ "\n 						and date(DataOkonchaniya)>=date(parameters.dataOtgruzki) "// 
				+ "\n 						and Nomenklatura=n.[_IDRRef] "// 
				+ "\n 				) and fx2.PoluchatelSkidki=kontragenty.GolovnoyKontragent "// 
				+ "\n 				and fx2.Nomenklatura=n.[_IDRRef] "//	
				+ "\n 	left join SkidkaPartneraKarta on SkidkaPartneraKarta.PoluchatelSkidki=n.[_IDRRef] "//					
				+ "\n 				and SkidkaPartneraKarta.Period=(select max(Period) from SkidkaPartneraKarta "//		
				+ "\n 					where PoluchatelSkidki=n.[_IDRRef] "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "// 
				+ "\n 					and date(DataOkonchaniya)>=date(parameters.dataOtgruzki) "//
				+ "\n 					) "//
				+ "\n 	left join NakopitelnyeSkidki on NakopitelnyeSkidki.PoluchatelSkidki=n.[_IDRRef] "//					
				+ "\n 				and NakopitelnyeSkidki.Period=(select max(Period) from NakopitelnyeSkidki "//		
				+ "\n 					where PoluchatelSkidki=parameters.kontragent "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "//
				+ "\n 					) "//	
				+ "\n 	join DogovoryKontragentov on DogovoryKontragentov.vladelec=parameters.kontragent "//
				+ "\n 	left join Prodazhi on Prodazhi.DogovorKontragenta=DogovoryKontragentov._IDRref "// 
				+ "\n 				and Prodazhi.nomenklatura=n.[_IDRRef] "// 
				+ "\n 				and Prodazhi.period=(select max(period) from prodazhi where nomenklatura=n.[_IDRRef]) "//	
				+ "\n 	left join AdresaPoSkladam skladKazan on skladKazan.nomenklatura=n._idrref and skladKazan.Traphik=x'00' "//
				//+ "\n  				and skladKazan.period=(select max(period) from adresaposkladam "// 
				//+ "\n  					where nomenklatura=n._idrref "// 
				+ "\n 					and skladKazan.baza=x'A756BEA77AB71E2F45CD824C4AB4178F'  "//
				//+ "\n 					and Traphik=x'00' "//
				//+ "\n 					) "//
				+ "\n 	left join AdresaPoSkladam skladHoreca on skladHoreca.nomenklatura=n._idrref and skladHoreca.Traphik=x'00' "//
				//+ "\n  				and skladHoreca.period=(select max(period) from adresaposkladam "// 
				//+ "\n  					where nomenklatura=n._idrref "// 
				+ "\n 					and skladHoreca.baza=x'AAFFF658AE67DCE94696B419219D8E1C' "// 
				//+ "\n 					and Traphik=x'00' "//
				//+ "\n 					) "//	
				+ "\n where ( n.[UpperName] like '%СЫР%') "// 
				//+ "\n group by n.[_idrref] "//  
				+ "\n ORDER BY n.[naimenovanie] "//  
				+ "\n limit 1000 offset 100; "//
		;
		//System.out.println("sql: " + sql);
		Cursor ex = db().rawQuery(sql, null);
		System.out.println("read");
		Bough b = //new Bough();
		Auxiliary.fromCursor(ex);
		System.out.println("rows: " + b.children.size());
		//b.child("test").child("test").value.is("123");
		//b.value.is("123");
		//System.out.println(b.dumpXML());
	}
	void testReadNew() {
		System.out.println("testReadNew");
		String sql = " select n._id "//					
				//+ "\n 	,n.[_IDRRef] "//					
				+ "\n 	,n.[Artikul] "//					
				+ "\n 	,n.[Naimenovanie] "//	
				
				+ "\n 	,n.[OsnovnoyProizvoditel] "//					
				+ "\n 	,p.Naimenovanie as ProizvoditelNaimenovanie "//
				+ "\n 	,CenyNomenklaturySklada.Cena as Cena "//
				+ "\n 	,0 as Skidka "//					
				+ "\n 	,0 as CenaSoSkidkoy "//					
				+ "\n 	,x'00' as VidSkidki "//	
				+ "\n 	,eho.[Naimenovanie] as [EdinicyIzmereniyaNaimenovanie] "//
				+ "\n 	,VelichinaKvantovNomenklatury.Kolichestvo as MinNorma "//
				+ "\n 	,ei.Koephphicient as [Koephphicient] "//					
				+ "\n  	,ei._IDRRef as [EdinicyIzmereniyaID] "//					
				+ "\n  	,n.Roditel as Roditel "//
				+ "\n 	,(1.0+ifnull(n1.nacenka,ifnull(n2.nacenka,ifnull(n3.nacenka,ifnull(n4.nacenka,n5.nacenka))))/100.0)*TekuschieCenyOstatkovPartiy.Cena as MinCena "//
				+ "\n 	,(1.0+const.MaksNacenkaCenyPraysa/100.0)*CenyNomenklaturySklada.Cena as MaxCena "//
				+ "\n 	,TekuschieCenyOstatkovPartiy.cena as BasePrice "//
				+ "\n 	,Prodazhi.Stoimost/Prodazhi.kolichestvo as LastPrice "//
				+ "\n 	,ifnull(nk1.ProcentSkidkiNacenki,nk2.ProcentSkidkiNacenki) as Nacenka "//
				+ "\n 	,ZapretSkidokTov.Individualnye as ZapretSkidokTovIndividualnye "//
				+ "\n 	,ZapretSkidokTov.Nokopitelnye as ZapretSkidokTovNokopitelnye "//
				+ "\n 	,ZapretSkidokTov.Partner as ZapretSkidokTovPartner "//
				+ "\n 	,ZapretSkidokTov.Razovie as ZapretSkidokTovRazovie "//
				+ "\n 	,ZapretSkidokTov.Nacenki as ZapretSkidokTovNacenki "//
				+ "\n 	,ZapretSkidokProizv.Individualnye as ZapretSkidokProizvIndividualnye "//
				+ "\n 	,ZapretSkidokProizv.Nokopitelnye as ZapretSkidokProizvNokopitelnye "//
				+ "\n 	,ZapretSkidokProizv.Partner as ZapretSkidokProizvPartner "//
				+ "\n 	,ZapretSkidokProizv.Razovie as ZapretSkidokProizvRazovie "//
				+ "\n 	,ZapretSkidokProizv.Nacenki as ZapretSkidokProizvNacenki "//
				+ "\n 	,ifnull(fx1.FixCena,fx2.FixCena) as FiksirovannyeCeny "//
				+ "\n 	,SkidkaPartneraKarta.ProcentSkidkiNacenki as SkidkaPartneraKarta "//
				+ "\n 	,NakopitelnyeSkidki.ProcentSkidkiNacenki as NakopitelnyeSkidki "//
				+ "\n 	,Prodazhi.period as LastSell "// 
				+ "\n 	,skladKazan.sklad as skladKazan "//
				+ "\n 	,skladHoreca.sklad as skladHoreca "//
				
				+ "\n  	from Nomenklatura_sorted n "//	
				+ "\n 	join Consts const "// 
				+ "\n 	join (select "// 
				+ "\n 			'2013-01-30' as dataOtgruzki "//
				+ "\n 			,x'935B18A90562E07411E1BC64CF4E1505' as kontragent "//
				+ "\n 			,x'B443002264FA89D811E02D3AF097290A' as polzovatel "//
				+ "\n 		) parameters "//		
				+ "\n 	join TekuschieCenyOstatkovPartiy_strip TekuschieCenyOstatkovPartiy on TekuschieCenyOstatkovPartiy.nomenklatura=n.[_IDRRef] "//
				+ "\n 	join Polzovateli on Polzovateli._idrref=parameters.polzovatel "//
				+ "\n 	join Podrazdeleniya p1 on p1._idrref=Polzovateli.podrazdelenie "//
				+ "\n 	join EdinicyIzmereniya_strip eho on n.EdinicaKhraneniyaOstatkov = eho._IDRRef "//			
				+ "\n 	join kontragenty on kontragenty._idrref=parameters.kontragent "//		
				+ "\n 	join EdinicyIzmereniya_strip ei on n.EdinicaDlyaOtchetov = ei._IDRRef "//	
				+ "\n 	join CenyNomenklaturySklada_last CenyNomenklaturySklada on CenyNomenklaturySklada.nomenklatura=n.[_IDRRef] "// 
				//+ "\n 		and CenyNomenklaturySklada.Period=(select max(Period) from CenyNomenklaturySklada "//			
				//+ "\n 				where nomenklatura=n.[_IDRRef] "// 
				//+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "//
				//+ "\n 				) "//	
				+ "\n 	join DogovoryKontragentov on DogovoryKontragentov.vladelec=parameters.kontragent "//
				+ "\n 	left join NacenkiKontr nk1 on nk1.PoluchatelSkidki=parameters.kontragent "// 
				+ "\n 			and nk1.Period=(select max(Period) from NacenkiKontr "// 
				+ "\n 				where PoluchatelSkidki=parameters.kontragent "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "// "//
				+ "\n 				) "//
				+ "\n 	left join NacenkiKontr nk2 on nk2.PoluchatelSkidki=kontragenty.GolovnoyKontragent "// 
				+ "\n 			and nk2.Period=(select max(Period) from NacenkiKontr "// 
				+ "\n 				where PoluchatelSkidki=kontragenty.GolovnoyKontragent "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "//
				+ "\n 					) "//
				+ "\n 	left join ZapretSkidokTov on ZapretSkidokTov.Nomenklatura=n.[_IDRRef] "// 
				+ "\n 			and ZapretSkidokTov.Period=(select max(Period) from ZapretSkidokTov "// 
				+ "\n 				where Nomenklatura=n.[_IDRRef] "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "//
				+ "\n 				) "//
				+ "\n 	left join ZapretSkidokProizv on ZapretSkidokProizv.Proizvoditel=n.[OsnovnoyProizvoditel] "//					
				+ "\n 				and ZapretSkidokProizv.Period=(select max(Period) from ZapretSkidokProizv "//		
				+ "\n 					where Proizvoditel=n.[OsnovnoyProizvoditel] "// 
				+ "\n 						and date(period)<=date(parameters.dataOtgruzki) "//
				+ "\n 					) "//
				+ "\n 	left join Podrazdeleniya p2  on p1.roditel=p2._idrref "//
				+ "\n 	left join Podrazdeleniya p3  on p2.roditel=p3._idrref "//
				+ "\n 	left join Podrazdeleniya p4  on p3.roditel=p4._idrref "//
				+ "\n 	left join MinimalnyeNacenkiProizvoditeley_1 n1  on n1.podrazdelenie=p1._idrref "// 
				+ "\n 				and n1.NomenklaturaProizvoditel_2=n._idrref "// 
				+ "\n 				and n1.period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 "// 
				+ "\n 					where podrazdelenie=p1._idrref "// 
				+ "\n 					and NomenklaturaProizvoditel_2=n._idrref "//
				+ "\n 					) "//
				+ "\n 	left join MinimalnyeNacenkiProizvoditeley_1 n2  on n2.podrazdelenie=p2._idrref "// 
				+ "\n 				and n2.NomenklaturaProizvoditel_2=n._idrref "// 
				+ "\n 				and n2.period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 "// 
				+ "\n 					where podrazdelenie=p2._idrref "// 
				+ "\n 					and NomenklaturaProizvoditel_2=n._idrref "//
				+ "\n 					) "//
				+ "\n 	left join MinimalnyeNacenkiProizvoditeley_1 n3  on n3.podrazdelenie=p3._idrref "// 
				+ "\n 				and n3.NomenklaturaProizvoditel_2=n._idrref "// 
				+ "\n 				and n3.period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 "// 
				+ "\n 					where podrazdelenie=p3._idrref "// 
				+ "\n 					and NomenklaturaProizvoditel_2=n._idrref "//
				+ "\n 					) "//
				+ "\n 	left join MinimalnyeNacenkiProizvoditeley_1 n4  on n4.podrazdelenie=p4._idrref "// 
				+ "\n 				and n4.NomenklaturaProizvoditel_2=n._idrref "// 
				+ "\n 				and n4.period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 "// 
				+ "\n 					where podrazdelenie=p4._idrref "// 
				+ "\n 					and NomenklaturaProizvoditel_2=n._idrref "//
				+ "\n 					) "//
				+ "\n 	left join MinimalnyeNacenkiProizvoditeley_1 n5  on n5.podrazdelenie=X'00000000000000000000000000000000' "// 
				+ "\n 				and n5.NomenklaturaProizvoditel_2=n._idrref "// 
				+ "\n 				and n5.period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 "// 
				+ "\n 					where podrazdelenie=X'00000000000000000000000000000000' "// 
				+ "\n 					and NomenklaturaProizvoditel_2=n._idrref "//
				+ "\n 					) "//	
				+ "\n 	left join Proizvoditel p on n.[OsnovnoyProizvoditel] = p._IDRRef "//
				+ "\n 	left join VelichinaKvantovNomenklatury on VelichinaKvantovNomenklatury.nomenklatura=n.[_IDRRef] "// 
				+ "\n 	left join FiksirovannyeCeny fx1 on fx1.DataOkonchaniya=( "// 
				+ "\n 					select max(DataOkonchaniya) from FiksirovannyeCeny "// 
				+ "\n 						where PoluchatelSkidki=parameters.kontragent "// 
				+ "\n 						and date(period)<=date(parameters.dataOtgruzki) "// 
				+ "\n 						and date(DataOkonchaniya)>=date(parameters.dataOtgruzki) "// 
				+ "\n 						and Nomenklatura=n.[_IDRRef] "// 
				+ "\n 				) and fx1.PoluchatelSkidki=parameters.kontragent "// 
				+ "\n 				and fx1.Nomenklatura=n.[_IDRRef] "// 
				+ "\n 	left join FiksirovannyeCeny fx2 on fx2.DataOkonchaniya=( "// 
				+ "\n 					select max(DataOkonchaniya) from FiksirovannyeCeny "// 
				+ "\n 						where PoluchatelSkidki=kontragenty.GolovnoyKontragent "// 
				+ "\n 						and date(period)<=date(parameters.dataOtgruzki) "// 
				+ "\n 						and date(DataOkonchaniya)>=date(parameters.dataOtgruzki) "// 
				+ "\n 						and Nomenklatura=n.[_IDRRef] "// 
				+ "\n 				) and fx2.PoluchatelSkidki=kontragenty.GolovnoyKontragent "// 
				+ "\n 				and fx2.Nomenklatura=n.[_IDRRef] "//	
				+ "\n 	left join SkidkaPartneraKarta on SkidkaPartneraKarta.PoluchatelSkidki=n.[_IDRRef] "//					
				+ "\n 				and SkidkaPartneraKarta.Period=(select max(Period) from SkidkaPartneraKarta "//		
				+ "\n 					where PoluchatelSkidki=n.[_IDRRef] "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "// 
				+ "\n 					and date(DataOkonchaniya)>=date(parameters.dataOtgruzki) "//
				+ "\n 					) "//
				+ "\n 	left join NakopitelnyeSkidki on NakopitelnyeSkidki.PoluchatelSkidki=n.[_IDRRef] "//					
				+ "\n 				and NakopitelnyeSkidki.Period=(select max(Period) from NakopitelnyeSkidki "//		
				+ "\n 					where PoluchatelSkidki=parameters.kontragent "// 
				+ "\n 					and date(period)<=date(parameters.dataOtgruzki) "//
				+ "\n 					) "//	
				+ "\n 	left join Prodazhi on Prodazhi.DogovorKontragenta=DogovoryKontragentov._IDRref "// 
				+ "\n 				and Prodazhi.nomenklatura=n.[_IDRRef] "// 
				+ "\n 				and Prodazhi.period=(select max(period) from prodazhi where nomenklatura=n.[_IDRRef]) "//	
				+ "\n 	 join AdresaPoSkladam_last skladKazan on skladKazan.nomenklatura=n._idrref and skladKazan.Traphik=x'00' "//
				//+ "\n  				and skladKazan.period=(select max(period) from adresaposkladam "// 
				//+ "\n  					where nomenklatura=n._idrref "// 
				+ "\n 					and skladKazan.baza=x'A756BEA77AB71E2F45CD824C4AB4178F'  "//
				//+ "\n 					and Traphik=x'00' "//
				//+ "\n 					) "//
				+ "\n 	left join AdresaPoSkladam_last skladHoreca on skladHoreca.nomenklatura=n._idrref and skladHoreca.Traphik=x'00' "//
				//+ "\n  				and skladHoreca.period=(select max(period) from adresaposkladam "// 
				//+ "\n  					where nomenklatura=n._idrref "// 
				+ "\n 					and skladHoreca.baza=x'AAFFF658AE67DCE94696B419219D8E1C' "// 
				//+ "\n 					and Traphik=x'00' "//
				//+ "\n 					) "//	
				//+ "\n where ( n.[UpperName] like '%СЫР%')"// and n._id>=2193"// 
				//+ "\n group by n.[_idrref] "//  
				//+ "\n ORDER BY n.[_id] "//  
				+ "\n limit 112  "//offset 100; "//
		;
		//sql="select _id,Naimenovanie from nomenklatura limit 1000";
		//System.out.println("sql: " + sql);
		Cursor ex = db().rawQuery(sql, null);
		System.out.println("read");
		 testData = //new Bough();
		Auxiliary.fromCursor(ex);
		System.out.println("rows: " + testData.children.size());
		//b.child("test").child("test").value.is("123");
		//b.value.is("123");
		//System.out.println(b.dumpXML());
	}
	public static void setupStrippedData(SQLiteDatabase mDB) {
		System.out.println("Nomenklatura_sorted");
		mDB.execSQL("	drop table if exists Nomenklatura_sorted;	");
		mDB.execSQL("	CREATE TABLE Nomenklatura_sorted (	"
				+ "\n		_id integer primary key asc autoincrement, [_IDRRef] blob null,[_Version] timestamp null,[PometkaUdaleniya] blob null,[Predopredelennyy] blob null	"//
				+ "\n		,[Roditel] blob null,[EtoGruppa] blob null,[Kod] nchar (10) null,[Naimenovanie] nvarchar (160) null,[NaimenovaniePolnoe] text null	"//
				+ "\n		,[Artikul] nvarchar (50) null,[EdinicaKhraneniyaOstatkov] blob null,[BazovayaEdinicaIzmereniya] blob null,[StavkaNDS] blob null,[Kommentariy] text null	"//
				+ "\n		,[Usluga] blob null,[Nabor] blob null,[NomenklaturnayaGruppa] blob null,[NomenklaturnayaGruppaZatrat] blob null,[VesovoyKoephphicientVkhozhdeniya] numeric null	"//
				+ "\n		,[VestiUchetPoKharakteristikam] blob null,[OtvetstvennyyMenedzherZaPokupki] blob null,[OsnovnoyPostavschik] blob null,[StatyaZatrat] blob null	"//
				+ "\n		,[OsnovnoeIzobrazhenie] blob null,[StranaProiskhozhdeniya] blob null,[NomerGTD] blob null,[EdinicaDlyaOtchetov] blob null,[Vesovoy] blob null	"//
				+ "\n		,[PoryadokPriPechatiPraysLista] numeric null,[OsnovnoyProizvoditel] blob null,[Prioritet] numeric null,[MinimalnyyOstatok] numeric null,[VetKategoriya] blob null	"//
				+ "\n		,[ProcentEstestvennoyUbyli] numeric null,[SrokGodnosti] numeric null,[TovarPodZakaz] blob null,[Brend] blob null,[TovarPodZakazKazan] blob null, UpperName text null	"//
				+ "\n	);	");
		mDB.execSQL("	CREATE INDEX IX_Nomenklatura_sorted on Nomenklatura_sorted(_IDRRef);	");
		mDB.execSQL("	CREATE INDEX IX_Nomenklatura_sorted_Artikul on Nomenklatura_sorted(Artikul);	");
		mDB.execSQL("	CREATE INDEX [IX_Nomenklatura_sorted_EdenicaKhraneniyaOstatkov] ON Nomenklatura_sorted([EdinicaKhraneniyaOstatkov]);	");
		mDB.execSQL("	CREATE INDEX [IX_Nomenklatura_sorted_EdinicaDlyaOtchetov] ON Nomenklatura_sorted([EdinicaDlyaOtchetov]);	");
		mDB.execSQL("	CREATE INDEX IX_Nomenklatura_sorted_IDRref on Nomenklatura_sorted(_IDRRef);	");
		mDB.execSQL("	CREATE INDEX [IX_Nomenklatura_sorted_OsnovnoyProizvoditel] ON Nomenklatura_sorted([OsnovnoyProizvoditel]);	");
		mDB.execSQL("	insert into Nomenklatura_sorted (	"//
				+ "\n			_IDRRef,_Version,PometkaUdaleniya,Predopredelennyy,Roditel,EtoGruppa,Kod,Naimenovanie,NaimenovaniePolnoe,Artikul,EdinicaKhraneniyaOstatkov	"//
				+ "\n			,BazovayaEdinicaIzmereniya,StavkaNDS,Kommentariy,Usluga,Nabor,NomenklaturnayaGruppa,NomenklaturnayaGruppaZatrat	"//
				+ "\n			,VesovoyKoephphicientVkhozhdeniya,VestiUchetPoKharakteristikam,OtvetstvennyyMenedzherZaPokupki,OsnovnoyPostavschik,StatyaZatrat	"//
				+ "\n			,OsnovnoeIzobrazhenie,StranaProiskhozhdeniya,NomerGTD,EdinicaDlyaOtchetov,Vesovoy,PoryadokPriPechatiPraysLista,OsnovnoyProizvoditel	"//
				+ "\n			,Prioritet,MinimalnyyOstatok,VetKategoriya,ProcentEstestvennoyUbyli,SrokGodnosti,TovarPodZakaz,Brend,TovarPodZakazKazan,UpperName	"//
				+ "\n			)	"//
				+ "\n		select 	"//
				+ "\n			_IDRRef,_Version,PometkaUdaleniya,Predopredelennyy,Roditel,EtoGruppa,Kod,Naimenovanie,NaimenovaniePolnoe,Artikul,EdinicaKhraneniyaOstatkov	"//
				+ "\n			,BazovayaEdinicaIzmereniya,StavkaNDS,Kommentariy,Usluga,Nabor,NomenklaturnayaGruppa,NomenklaturnayaGruppaZatrat	"//
				+ "\n			,VesovoyKoephphicientVkhozhdeniya,VestiUchetPoKharakteristikam,OtvetstvennyyMenedzherZaPokupki,OsnovnoyPostavschik,StatyaZatrat	"//
				+ "\n			,OsnovnoeIzobrazhenie,StranaProiskhozhdeniya,NomerGTD,EdinicaDlyaOtchetov,Vesovoy,PoryadokPriPechatiPraysLista,OsnovnoyProizvoditel	"//
				+ "\n			,Prioritet,MinimalnyyOstatok,VetKategoriya,ProcentEstestvennoyUbyli,SrokGodnosti,TovarPodZakaz,Brend,TovarPodZakazKazan,UpperName	"//
				+ "\n		from nomenklatura n	"//
				+ "\n		join AdresaPoSkladam on AdresaPoSkladam.nomenklatura=n._idrref	"//
				+ "\n			and AdresaPoSkladam.sklad<>X'00000000000000000000000000000000'	"//
				+ "\n			and AdresaPoSkladam.period=(select max(period) from AdresaPoSkladam where AdresaPoSkladam.nomenklatura=n._idrref)	"//
				+ "\n		group by n._idrref	"//
				+ "\n		order by naimenovanie	"//
				+ "\n	;	");
		mDB.execSQL("	drop table if exists AdresaPoSkladam_last;	");
		mDB.execSQL("	CREATE TABLE AdresaPoSkladam_last (	"
				+ "\n	_id integer primary key asc autoincrement, [Period] date null,[Baza] blob null,[Nomenklatura] blob null,[Sklad] blob null,[Traphik] blob null	" + "\n	);	");
		mDB.execSQL("	CREATE INDEX IX_AdresaPoSkladam_last_Period on AdresaPoSkladam_last (Period);	");
		mDB.execSQL("	CREATE INDEX IX_AdresaPoSkladam_last_Nomenklatura on AdresaPoSkladam_last (Nomenklatura);	");
		mDB.execSQL("	CREATE INDEX IX_AdresaPoSkladam_last_Baza on AdresaPoSkladam_last (Baza);	");
		mDB.execSQL("	CREATE INDEX IX_AdresaPoSkladam_last_Sklad on AdresaPoSkladam_last (Sklad);	");
		mDB.execSQL("	CREATE INDEX IX_AdresaPoSkladam_last_Traphik on AdresaPoSkladam_last (Traphik);	");
		mDB.execSQL("	insert into AdresaPoSkladam_last (	"//
				+ "\n			Period,Baza,Nomenklatura,Sklad,Traphik	"//
				+ "\n		)	"//
				+ "\n		select 	"//
				+ "\n			Period,Baza,Nomenklatura,Sklad,Traphik	"//
				+ "\n		from AdresaPoSkladam a1	"//
				+ "\n			where a1.period=(select max(period) from AdresaPoSkladam a2	"//
				+ "\n				where a1.nomenklatura=a2.nomenklatura	"//
				+ "\n					and a1.baza=a2.baza	"//
				+ "\n				) and a1.sklad<>X'00000000000000000000000000000000'	"//
				+ "\n		group by a1.baza,a1.sklad,a1.nomenklatura	"//
				+ "\n	;	");
		mDB.execSQL("	drop table if exists CenyNomenklaturySklada_last;	");
		mDB.execSQL("	CREATE TABLE CenyNomenklaturySklada_last (	"//
				+ "\n	_id integer primary key asc autoincrement,[Period] date null,[Registrator] blob null,[NomerStroki] numeric null,[Aktivnost] blob null	"//
				+ "\n	,[TipCen] blob null,[Nomenklatura] blob null,[Sklad] blob null,[Valyuta] blob null,[Cena] numeric null,[EdinicaIzmereniya] blob null	"//
				+ "\n	,[ProcentSkidkiNacenki] numeric null	"//
				+ "\n	);	");
		mDB.execSQL("	CREATE INDEX IX_CenyNomenklaturySklada_last_Nomenklatura on CenyNomenklaturySklada_last(Nomenklatura);	");
		mDB.execSQL("	CREATE INDEX IX_CenyNomenklaturySklada_last_Period on CenyNomenklaturySklada_last(Period);	");
		mDB.execSQL("	CREATE INDEX IX_CenyNomenklaturySklada_last_Cena on CenyNomenklaturySklada_last(Cena);	");
		mDB.execSQL("	insert into CenyNomenklaturySklada_last (	"//
				+ "\n	Period,Registrator,NomerStroki,Aktivnost,TipCen,Nomenklatura,Sklad,Valyuta,Cena,EdinicaIzmereniya,ProcentSkidkiNacenki	"//
				+ "\n	)	"//
				+ "\n	select 	"//
				+ "\n	Period,Registrator,NomerStroki,Aktivnost,TipCen,Nomenklatura,Sklad,Valyuta,Cena,EdinicaIzmereniya,ProcentSkidkiNacenki	"//
				+ "\n	from CenyNomenklaturySklada c1	"//
				+ "\n	join Nomenklatura_sorted on Nomenklatura_sorted._idrref=c1.nomenklatura	"//
				+ "\n	where c1.Period=(select max(c2.Period) from CenyNomenklaturySklada c2 where c1.nomenklatura=c2.nomenklatura)	"//
				+ "\n	group by nomenklatura	"//
				+ "\n	;	");
		mDB.execSQL("	drop table if exists EdinicyIzmereniya_strip;	");
		mDB.execSQL("	CREATE TABLE EdinicyIzmereniya_strip (	"//
				+ "\n	_id integer primary key asc autoincrement, [_IDRRef] blob null,[_Version] timestamp null,[PometkaUdaleniya] blob null	"//
				+ "\n	,[Predopredelennyy] blob null,[Vladelec_0] blob null,[Vladelec_1] blob null,[Vladelec_2] blob null,[Kod] nchar (12) null	"//
				+ "\n	,[Naimenovanie] nvarchar (50) null,[EdinicaPoKlassiphikatoru] blob null,[Ves] numeric null,[Obem] numeric null,[DolyaPallety] numeric null	"//
				+ "\n	,[Koephphicient] numeric null,[DolyaPalletyDlyaOtgruzki] numeric null	"//
				+ "\n	);	");
		mDB.execSQL("	CREATE INDEX IX_EdinicyIzmereniya_strip_IDRRef on EdinicyIzmereniya_strip(_IDRRef);	");
		mDB.execSQL("	insert into EdinicyIzmereniya_strip (	"//
				+ "\n	_IDRRef,_Version,PometkaUdaleniya	"//
				+ "\n	,Predopredelennyy,Vladelec_0,Vladelec_1,Vladelec_2,Kod,Naimenovanie,EdinicaPoKlassiphikatoru	"//
				+ "\n	,Ves,Obem,DolyaPallety,Koephphicient,DolyaPalletyDlyaOtgruzki	"//
				+ "\n	)	"//
				+ "\n	select 	"//
				+ "\n	ediz._IDRRef,ediz._Version,ediz.PometkaUdaleniya	"//
				+ "\n	,ediz.Predopredelennyy,ediz.Vladelec_0,ediz.Vladelec_1,ediz.Vladelec_2,ediz.Kod,ediz.Naimenovanie,ediz.EdinicaPoKlassiphikatoru	"//
				+ "\n	,ediz.Ves,ediz.Obem,ediz.DolyaPallety,ediz.Koephphicient,ediz.DolyaPalletyDlyaOtgruzki	"//
				+ "\n	from EdinicyIzmereniya ediz	"//
				+ "\n	join nomenklatura_sorted n on n.EdinicaDlyaOtchetov=ediz._idrref or n.EdinicaKhraneniyaOstatkov=ediz._idrref	"//
				+ "\n	group by ediz._idrref	"//
				+ "\n	;	");
		mDB.execSQL("	drop table if exists TekuschieCenyOstatkovPartiy_strip;	");
		mDB.execSQL("	CREATE TABLE TekuschieCenyOstatkovPartiy_strip (	"//
				+ "\n	_id integer primary key asc autoincrement, [Nomenklatura] blob null,[Cena] numeric null,[UstanavlivaetsyaVruchnuyu] blob null	"//
				+ "\n	);	");
		mDB.execSQL("	CREATE INDEX IX_TekuschieCenyOstatkovPartiy_strip_Cena on TekuschieCenyOstatkovPartiy_strip(Cena);	");
		mDB.execSQL("	CREATE INDEX IX_TekuschieCenyOstatkovPartiy_strip_Nomenklatura on TekuschieCenyOstatkovPartiy_strip(nomenklatura);	");
		mDB.execSQL("	insert into TekuschieCenyOstatkovPartiy_strip (	"//
				+ "\n	Nomenklatura,Cena,UstanavlivaetsyaVruchnuyu	"//
				+ "\n	)	"//
				+ "\n	select 	"//
				+ "\n	Nomenklatura,Cena,UstanavlivaetsyaVruchnuyu	"//
				+ "\n	from TekuschieCenyOstatkovPartiy	"//
				+ "\n	join Nomenklatura_sorted n on n._idrref=TekuschieCenyOstatkovPartiy.nomenklatura	"//
				+ "\n	group by n._idrref	"//
				+ "\n	;	");
		mDB.execSQL("	drop table if exists VelichinaKvantovNomenklatury_strip;	");
		mDB.execSQL("	CREATE TABLE VelichinaKvantovNomenklatury_strip (	"//
				+ "\n	_id integer primary key asc autoincrement, [Nomenklatura] blob null,[Kvant] blob null,[Sklad] blob null,[Kolichestvo] numeric null,[_SimpleKey] blob null	"//
				+ "\n	);	");
		mDB.execSQL("	CREATE INDEX [IX_VelichinaKvantovNomenklatury_strip_Nomenklatura] ON [VelichinaKvantovNomenklatury_strip] ([Nomenklatura]);	");
		mDB.execSQL("	insert into VelichinaKvantovNomenklatury_strip (	"//
				+ "\n	Nomenklatura,Kvant,Sklad,Kolichestvo,_SimpleKey	"//
				+ "\n	)	"//
				+ "\n	select	"//
				+ "\n	Nomenklatura,Kvant,Sklad,Kolichestvo,_SimpleKey	"//
				+ "\n	from VelichinaKvantovNomenklatury	"//
				+ "\n	join nomenklatura_sorted on nomenklatura_sorted._idrref=VelichinaKvantovNomenklatury.Nomenklatura	"//
				+ "\n	group by VelichinaKvantovNomenklatury.Nomenklatura	"//
				+ "\n	;	");
		mDB.execSQL("	drop table if exists Prodazhi_last;	");
		mDB.execSQL("	CREATE TABLE Prodazhi_last (	"//
				+ "\n	_id integer primary key asc autoincrement, [Period] date null,[Registrator_0] blob null,[Registrator_1] blob null,[NomerStroki] numeric null	"//
				+ "\n	,[Aktivnost] blob null,[Nomenklatura] blob null,[KharakteristikaNomenklatury] blob null,[ZakazPokupatelya_0] blob null,[ZakazPokupatelya_1] blob null	"//
				+ "\n	,[ZakazPokupatelya_2] blob null,[DogovorKontragenta] blob null,[DokumentProdazhi_0] blob null,[DokumentProdazhi_1] blob null	"//
				+ "\n	,[DokumentProdazhi_2] blob null,[Proekt] blob null,[Podrazdelenie] blob null,[Kolichestvo] numeric null,[Stoimost] numeric null	"//
				+ "\n	,[StoimostBezSkidok] numeric null,[Sebestoimost] numeric null,[SummaVozvrataNacenki] numeric null	"//
				+ "\n	);	");
		mDB.execSQL("	CREATE INDEX [IX_Prodazhi_last_Nomenklatura] ON [Prodazhi_last] ([Nomenklatura]);	");
		mDB.execSQL("	CREATE INDEX [IX_Prodazhi_last_Period] ON [Prodazhi_last] ([Period]);	");
		mDB.execSQL("	insert into Prodazhi_last (	"//
				+ "\n	Period,Registrator_0,Registrator_1,NomerStroki,Aktivnost,Nomenklatura,KharakteristikaNomenklatury,ZakazPokupatelya_0,ZakazPokupatelya_1	"//
				+ "\n	,ZakazPokupatelya_2,DogovorKontragenta,DokumentProdazhi_0,DokumentProdazhi_1,DokumentProdazhi_2,Proekt,Podrazdelenie,Kolichestvo	"//
				+ "\n	,Stoimost,StoimostBezSkidok,Sebestoimost,SummaVozvrataNacenki	"//
				+ "\n	)	"//
				+ "\n	select	"//
				+ "\n	Period,Registrator_0,Registrator_1,NomerStroki,Aktivnost,Nomenklatura,KharakteristikaNomenklatury,ZakazPokupatelya_0,ZakazPokupatelya_1	"//
				+ "\n	,ZakazPokupatelya_2,DogovorKontragenta,DokumentProdazhi_0,DokumentProdazhi_1,DokumentProdazhi_2,Proekt,Podrazdelenie,Kolichestvo	"//
				+ "\n	,Stoimost,StoimostBezSkidok,Sebestoimost,SummaVozvrataNacenki	"//
				+ "\n	from Prodazhi	"//
				+ "\n	join nomenklatura_sorted n on n._idrref=Prodazhi.Nomenklatura	"//
				+ "\n	and Prodazhi.period=(select max(period) from Prodazhi where Prodazhi.nomenklatura=n._idrref)	"//
				+ "\n	;	");
		mDB.execSQL("	analyze;	");
		System.out.println("strip done");
	}
	SQLiteDatabase db() {
		if (cacheSQLiteDatabase == null || (!cacheSQLiteDatabase.isOpen())) {
			//cacheSQLiteDatabase = Auxiliary.connectSQLiteDatabase("/sdcard/horeca/swlife_database", this);
			cacheSQLiteDatabase = this.openOrCreateDatabase("/sdcard/horeca/swlife_database", Context.MODE_PRIVATE, null);
		}
		//cacheSQLiteDatabase.setVersion(2);
		return cacheSQLiteDatabase;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("First");
		menu.add("Second");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
}
