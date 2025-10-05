package org.example.parser;
import jakarta.json.*;
import org.example.Main;
import org.example.modelo.Direccion;
import org.example.modelo.Empleado;
import org.example.modelo.Telefono;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ParserJSON {
    JsonReader reader = Json.createReader(Main.class.getResourceAsStream("/nuevo.json"));
    JsonStructure structure;
    public ParserJSON() {
        structure = reader.read();
    }

    private List<Empleado> obtenerEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        JsonObject estructuraRaiz = structure.asJsonObject();
        JsonArray datosArray = estructuraRaiz.getJsonArray("datos");

        for (JsonValue jsonValue : datosArray) {
            JsonObject datos = jsonValue.asJsonObject();
            Empleado emp =  new Empleado();
            emp.setId(datos.getInt("id"));
            emp.setNombre(datos.getString("firstName"));
            emp.setApellido(datos.getString("lastName"));
            emp.setEdad(datos.getInt("age"));
            emp.setDir(obtenerDir(datos.getJsonObject("address")));
            emp.setTelefonos(obtenerTelefonos(datos.getJsonArray("phoneNumbers")));
            empleados.add(emp);
        }
        return empleados;
    }

    private Direccion obtenerDir(JsonObject direccion){
        Direccion dire = new Direccion();
        dire.setCalle(direccion.getString("streetAddress"));
        dire.setCiudad(direccion.getString("city"));
        dire.setEstado(direccion.getString("state"));
        dire.setCp(direccion.getInt("postalCode"));
        return dire;
    }

    private List<Telefono> obtenerTelefonos(JsonArray telefonos) {
        List<Telefono> telefonoList = new ArrayList<>();
        Telefono telefono = new Telefono();
        for (int i = 0; i < telefonos.size(); i++) {
            JsonObject jsonTelefono = telefonos.getJsonObject(i);
            telefono.setTipo(jsonTelefono.getString("type"));
            telefono.setNumero(jsonTelefono.getString("number"));
            telefonoList.add(telefono);
        }
        return telefonoList;
    }
    public void guardarJson(){
        try(FileWriter file = new FileWriter("src/main/resources/nuevo.json")){
            JsonWriter writer = Json.createWriter(file);
            writer.write(structure);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void agregarEmp(Empleado em){
        JsonArrayBuilder empArray = Json.createArrayBuilder();
        JsonObject estruc = structure.asJsonObject();
        JsonArray datosArray = estruc.getJsonArray("datos");

        for(JsonValue dato : datosArray){
            empArray.add(dato);
        }
        JsonObjectBuilder datos =  Json.createObjectBuilder()
                .add("id",em.getId())
                .add("firstName", em.getNombre())
                .add("lastName", em.getApellido())
                .add("age", em.getEdad());
        JsonObjectBuilder dir = Json.createObjectBuilder()
                .add("streetAddress", em.getDir().getCalle())
                .add("city", em.getDir().getCiudad())
                .add("state", em.getDir().getEstado())
                .add("postalCode", em.getDir().getCp());
        JsonArrayBuilder array = Json.createArrayBuilder();
        for (Telefono telefono : em.getTelefonos()) {
            JsonObjectBuilder tel = Json.createObjectBuilder()
                    .add("type", telefono.getTipo())
                    .add("number", telefono.getNumero());
            array.add(tel);
        }
        JsonObjectBuilder empleado = datos
                .add("address", dir)
                .add("phoneNumbers", array);
        empArray.add(empleado);
        JsonObjectBuilder strucNuevo = Json.createObjectBuilder().
                add("datos",empArray);
        structure = strucNuevo.build();
        guardarJson();

        System.out.println("Empleado Agregado Exitosamente");

    }

    public  void  borrarEmpleado(){
        JsonPointer pointer = Json.createPointer("/datos1");
        structure = pointer.remove(structure);
    }
    private void eliminarEmpleado(int idEmp) {
        JsonObject datos1 = structure.asJsonObject();
        JsonArray empArray = datos1.getJsonArray("datos");
        JsonArrayBuilder nuevo = Json.createArrayBuilder();
        boolean buscar = false;
        for(JsonValue jsonValue : empArray){
            JsonObject emp = jsonValue.asJsonObject();
            int id = emp.getInt("id");
            if(id != idEmp){
                nuevo.add(emp);
            }else {
                buscar = true;
            }
        }
        if(buscar == true) {
            JsonObject nuevoObj = Json.createObjectBuilder().
                    add("datos", nuevo).build();
            structure = nuevoObj;
            guardarJson();
            System.out.println("Empleado con ID " + idEmp + " eliminado correctamente.");
        }
        else {
            System.out.println("Empleado no encontrado");
        }

    }
    private Empleado buscarEmpleado(int idEmp) {
        JsonObject datos = structure.asJsonObject(); //Objeto de raiz
        JsonArray empArray = datos.getJsonArray("datos");

        for (JsonValue jsonValue : empArray) {
            JsonObject emp = jsonValue.asJsonObject();
            int id = emp.getInt("id");

            if (id == idEmp) {
                Empleado empleado = new Empleado();
                empleado.setId(id);
                empleado.setNombre(emp.getString("firstName"));
                empleado.setApellido(emp.getString("lastName"));
                empleado.setEdad(emp.getInt("age"));
                JsonObject dirObj = emp.getJsonObject("address");
                if (dirObj != null) {
                    empleado.setDir(obtenerDir(dirObj));
                }
                JsonArray telefonosArray = emp.getJsonArray("phoneNumbers");
                if (telefonosArray != null) {
                    empleado.setTelefonos(obtenerTelefonos(telefonosArray));
                }
                System.out.println("Empleado Encontrado");
                System.out.println("firstName: " + empleado.getNombre() +
                        "\nlastName: " + empleado.getApellido() +
                        "\nage: " + empleado.getEdad());
                System.out.println("address: " +
                        "\n streetAddress: " + empleado.getDir().getCalle() +
                        "\n city: " + empleado.getDir().getCiudad() +
                        "\n state: " + empleado.getDir().getEstado() +
                        "\n postalCode: " + empleado.getDir().getCp());
                System.out.println("phoneNumbers:");
                for (Telefono t : empleado.getTelefonos()) {
                    System.out.println(" type: " + t.getTipo() + "\n number: " + t.getNumero());
                }

                return empleado;
            }
        }

        System.out.println("Empleado con ID " + idEmp + " no encontrado.");
        return null;
    }
    public void menu() {
        Scanner sc = new Scanner(System.in);
        int opc;
        int id;
        do {
            System.out.println("\n      GESTOR DE EMPLEADOS      ");
            System.out.println("1. Agregar Empleado");
            System.out.println("2. Eliminar Empleado");
            System.out.println("3. Buscar Empleado");
            System.out.println("4. Mostrar Empleados");
            System.out.println("5. Salir");
            System.out.print("Elige una opcion: ");
            opc = sc.nextInt();
            switch (opc) {
                case 1:
                    System.out.println("\n    Agregar Empleado    ");
                    System.out.print("ID: ");
                    id = sc.nextInt();
                    System.out.print("Nombre: ");
                    String nombre = sc.next();
                    System.out.print("Apellido: ");
                    String apellido = sc.next();
                    System.out.print("Edad: ");
                    int edad = sc.nextInt();

                    Direccion direccion = new Direccion();
                    System.out.print("Calle: ");
                    direccion.setCalle(sc.next());
                    System.out.print("Ciudad: ");
                    direccion.setCiudad(sc.next());
                    System.out.print("Estado: ");
                    direccion.setEstado(sc.next());
                    System.out.print("CP: ");
                    direccion.setCp(sc.nextInt());

                    Telefono telefono = new Telefono();
                    System.out.print("Tipo De Telefono: ");
                    telefono.setTipo(sc.next());
                    System.out.print("Numero: ");
                    telefono.setNumero(sc.next());

                    List<Telefono> telefonos = Arrays.asList(telefono);
                    Empleado empleado = new Empleado(id, nombre, apellido, edad, direccion, telefonos);
                    agregarEmp(empleado);
                    break;

                case 2:
                    System.out.println("\n    Eliminar Empleado    ");
                    System.out.print("Ingrese El ID Del Empleado: ");
                    id = sc.nextInt();
                    eliminarEmpleado(id);
                    break;

                case 3:
                    System.out.println("\n    Buscar Empleado    ");
                    System.out.print("Ingrese El ID Del Empleado: ");
                    id = sc.nextInt();
                    buscarEmpleado(id);
                    break;

                case 4:
                    System.out.println("\n    Lista de Empleados    ");
                    contenido();
                    break;

                case 5:
                    System.out.println("\n:)");
                    break;

                default:
                    System.out.println("\nOpccion Invalida.");
            }

        } while (opc != 5);

    }

    private void contenido() {
        JsonObject valores = structure.asJsonObject();
        JsonArray objeto = valores.getJsonArray("datos");
        for(JsonValue e :  objeto) {
            JsonObject valore = e.asJsonObject();
            for(String v : valore.keySet()) {
                System.out.println(v + ":");
                if (valore.get(v).getValueType() == JsonValue.ValueType.ARRAY) {
                    JsonArray array = (JsonArray) valore.getJsonArray(v);
                    for(JsonValue j : array){
                        JsonObject obj = (JsonObject) j;
                        for (String k : obj.keySet()) {
                            System.out.println(k + ": " );
                            System.out.println(obj.get(k));
                        }
                    }
                }else if (valore.get(v).getValueType() == JsonValue.ValueType.OBJECT) {
                    JsonObject objeto1 = (JsonObject) valore.get(v);
                    for(String e1 : objeto1.keySet()) {
                        System.out.println(e1 + ":");
                        System.out.println(objeto1.get(e1));
                    }
                } else {
                    System.out.println(valore.get(v));
                }
            }
        }
    }
}