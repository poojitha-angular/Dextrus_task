package com.prowess.dextrus.controllers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prowess.dextrus.common.CC;
import com.prowess.dextrus.entity.ConnectionEntity;
import com.prowess.dextrus.entity.RequestBodyPattern;
import com.prowess.dextrus.entity.RequestBodyQuery;
import com.prowess.dextrus.entity.TableDescription;
import com.prowess.dextrus.entity.TableType;
import com.prowess.dextrus.service.ConnectionService;

@RestController
@RequestMapping()
public class ConnectionController {

	@Autowired
	private ConnectionService service;

	@GetMapping("/get")
	public ResponseEntity<String> getSqlConnetcion(@RequestBody ConnectionEntity props) {
		Connection con = CC.getConnection(props);
		if (con == null)
			return new ResponseEntity<String>("Not Connected", HttpStatus.SERVICE_UNAVAILABLE);
		else

			return new ResponseEntity<String>("Connected to SQL Server", HttpStatus.OK);

	}


	@GetMapping("/")
	public ResponseEntity<List<String>> getCatalogList(@RequestBody ConnectionEntity props) {
		List<String> catalogs = service.getCatalogs(props);
		return new ResponseEntity<List<String>>(catalogs, HttpStatus.OK);

	}
	@GetMapping("/{catalog}")
	public ResponseEntity<List<String>> getSchemaList(@RequestBody ConnectionEntity props,@PathVariable String catalog){
		List<String> schemas=service.getSchema(props, catalog);
		
		return new ResponseEntity<List<String>>(schemas, HttpStatus.OK);
		
	}
	@GetMapping("/{catalog}/{schema}")
	public ResponseEntity<List<TableType>> getTablesAndViews(@RequestBody ConnectionEntity props,@PathVariable String catalog,@PathVariable String schema){
		List<TableType> tableviews =service.getViewsAndTables(props,catalog,schema);
		
		return new ResponseEntity<List<TableType>>(tableviews,HttpStatus.OK);
		
	}
	@GetMapping("/{catalog}/{schema}/{table}")
	public ResponseEntity<List<TableDescription>> getListOfColumns(@RequestBody ConnectionEntity props,@PathVariable String catalog,@PathVariable String schema,@PathVariable String table )
	{
		ArrayList<TableDescription> listofcolumns=service.getDescOfTables(props,catalog,schema,table);
		return new ResponseEntity<List<TableDescription>>(listofcolumns,HttpStatus.OK);
		
		
		
	}
	
	@PostMapping("/query")
	public ResponseEntity<List<List<Object>>> test(@RequestBody RequestBodyQuery qbody ) {
		ConnectionEntity prop = qbody.getProps();
		String query = qbody.getQuery();
		List<List<Object>> tableDataList = service.getTableData(prop, query);
		return new ResponseEntity<List<List<Object>>>(tableDataList, HttpStatus.OK);
	}
	@PostMapping("/search")
	public ResponseEntity<List<TableType>> getTablesByPattern(@RequestBody RequestBodyPattern bodyPattern){
		List<TableType> viewsAndTables = service.getTablesAndViewsByPattern(bodyPattern.getProperties(),bodyPattern.getCatalog(),bodyPattern.getPattern());
		return new ResponseEntity<List<TableType>>(viewsAndTables, HttpStatus.OK);
	}

}
