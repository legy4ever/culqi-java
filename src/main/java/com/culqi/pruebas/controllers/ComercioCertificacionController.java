package com.culqi.pruebas.controllers;

import com.culqi.commons.security.exception.CulqiSecurityException;
import com.culqi.pruebas.domain.NuevaVenta;
import com.culqi.sdk.Culqi;
import com.culqi.sdk.Pago;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping
public class ComercioCertificacionController {

    @Value("${culqi.url.mod_pago}")
    private String URLModuloPago;

    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ComercioCertificacionController.class);

    private SecureRandom random = new SecureRandom();

    public String randomOrder() {
        return new BigInteger(16, random).toString(4);
    }

    @RequestMapping(value = "/certificacion", method = RequestMethod.GET)
    public String validarVenta(Model model) throws IOException {

        LOGGER.info("Preparando información de venta para enviar a validación.");

        Culqi.llaveSecreta = "zzmxZlgIJtKKy0F71DMsZPWnPVzow4S90abBScLDIrk=";
        Culqi.codigoComercio = "testc101";
        Culqi.servidorBase = URLModuloPago;

        model.addAttribute("URLModuloPago", URLModuloPago);

        model.addAttribute("llave_secreta",Culqi.llaveSecreta );
        model.addAttribute("codigo_comercio_info",Culqi.codigoComercio );
        model.addAttribute("servidorBase",Culqi.servidorBase );

        String orden = randomOrder();

        String moneda = "PEN";

        Integer monto = random.nextInt(99900)+100;

        Float montoFlotante = (float)monto / 100;
        String montoFormateado = new DecimalFormat("0.00").format(montoFlotante);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Pago.PARAM_NUMERO_PEDIDO, orden);
        params.put(Pago.PARAM_MONEDA, moneda);
        params.put(Pago.PARAM_MONTO, monto);
        params.put(Pago.PARAM_DESCRIPCION, "Venta de prueba.");

        params.put(Pago.PARAM_CIUDAD, "Lima");
        params.put(Pago.PARAM_COD_PAIS, "PE");
        params.put(Pago.PARAM_DIRECCION, "Avenida Lima 1234");
        params.put(Pago.PARAM_NUM_TEL, "1029032132");

        model.addAttribute("informacion_transaccion_descifrada", params);

        model.addAttribute("numero_pedido", orden);
        model.addAttribute("moneda", moneda);
        model.addAttribute("monto", montoFormateado);
        model.addAttribute("descripción", "Venta de prueba.");
        model.addAttribute("codigoComercio", "testc101");

        Map<String, Object> respuesta = com.culqi.sdk.Pago.crearDatosPago(params);

        if (respuesta.get("codigo_respuesta").equals("OK") ) {

            model.addAttribute("respuesta", respuesta);

            model.addAttribute("informacion_venta", respuesta.get("informacion_venta"));
            model.addAttribute("codigo_comercio", respuesta.get("codigo_comercio"));

            Map<String, Object> consulta = Pago.consultar((String)respuesta.get("token"));

            model.addAttribute("estado_transaccion", consulta.get("estado_transaccion"));

            return "comercio_certificacion.html";

        } else {

            return "comercio_certificacion_error.html";
        }

    }

    @RequestMapping(value = "/efe.html", method = RequestMethod.GET)
    public String validarTiendasEfe(Model model) throws IOException {

        LOGGER.info("Tiendas EFE.");

        Culqi.llaveSecreta = "zzmxZlgIJtKKy0F71DMsZPWnPVzow4S90abBScLDIrk=";
        Culqi.codigoComercio = "testc102";
        Culqi.servidorBase = URLModuloPago;

        model.addAttribute("URLModuloPago", URLModuloPago);

        model.addAttribute("llave_secreta",Culqi.llaveSecreta );
        model.addAttribute("codigo_comercio_info",Culqi.codigoComercio );
        model.addAttribute("servidorBase",Culqi.servidorBase );

        String orden = randomOrder();

        String moneda = "PEN";

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Pago.PARAM_NUMERO_PEDIDO, orden);
        params.put(Pago.PARAM_MONEDA, moneda);
        params.put(Pago.PARAM_MONTO, 549900);
        params.put(Pago.PARAM_DESCRIPCION, "Televisor LED 55\" SAMSUNG + GRATIS: tablet Olitec 4 núcleos.");

        Map<String, Object> respuesta = com.culqi.sdk.Pago.crearDatosPago(params);

        if (respuesta.get("codigo_respuesta").equals("OK") ) {

            model.addAttribute("respuesta", respuesta);

            model.addAttribute("informacion_venta", respuesta.get("informacion_venta"));
            model.addAttribute("codigo_comercio", respuesta.get("codigo_comercio"));

            return "efe.html";

        } else {

            return "comercio_certificacion_error.html";
        }

    }

    @RequestMapping(value = "/bebemarket", method = RequestMethod.GET)
    public String validarBebeMarket(Model model) throws IOException {

        LOGGER.info("Preparando información de venta para enviar a validación.");

        Culqi.llaveSecreta = "zzmxZlgIJtKKy0F71DMsZPWnPVzow4S90abBScLDIrk=";
        Culqi.codigoComercio = "testc105";
        Culqi.servidorBase = URLModuloPago;

        model.addAttribute("URLModuloPago", URLModuloPago);

        String orden = randomOrder();

        String moneda = "PEN";

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(Pago.PARAM_NUMERO_PEDIDO, orden);
        params.put(Pago.PARAM_MONEDA, moneda);
        params.put(Pago.PARAM_MONTO, 8700);
        params.put(Pago.PARAM_DESCRIPCION, "Ropa para niños.");

        params.put(Pago.PARAM_CIUDAD, "Lima");
        params.put(Pago.PARAM_COD_PAIS, "PE");
        params.put(Pago.PARAM_DIRECCION, "Avenida Lima 1234");
        params.put(Pago.PARAM_NUM_TEL, "1029032132");


        Map<String, Object> respuesta = com.culqi.sdk.Pago.crearDatosPago(params);

        if (respuesta.get("codigo_respuesta").equals("OK") ) {

            model.addAttribute("respuesta", respuesta);

            model.addAttribute("informacion_venta", respuesta.get("informacion_venta"));
            model.addAttribute("codigo_comercio", respuesta.get("codigo_comercio"));

            return "bebe-market.html";

        } else {

            return "error.html";
        }

    }


}