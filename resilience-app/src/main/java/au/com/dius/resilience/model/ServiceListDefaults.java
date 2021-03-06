package au.com.dius.resilience.model;

import au.com.justinb.open311.model.ServiceList;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class ServiceListDefaults {

  public static final List<ServiceList> DEFAULT_SERVICES;

  static {{
    DEFAULT_SERVICES = new ImmutableList.Builder<ServiceList>()
      .add(new ServiceList.Builder().serviceCode("001")
        .serviceName("Road Blockage")
        .description("A road has been blocked.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("002")
        .serviceName("Dwelling Damage")
        .description("A dwelling has been damaged.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("003")
        .serviceName("Outbuilding Damage")
        .description("An outbuilding has been damaged.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("004")
        .serviceName("Fencing Damage")
        .description("A fence has been damaged.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("005")
        .serviceName("Driveway Blockage")
        .description("A driveway has been blocked.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("006")
        .serviceName("Vehicle Damage")
        .description("A vehicle has been damaged.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("007")
        .serviceName("Crop Damage/Loss")
        .description("A crop has been lost or damaged.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("008")
        .serviceName("Public Building Damage")
        .description("A public building has been damaged.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("009")
        .serviceName("Commercial Building Damage")
        .description("A commercial building has been damaged.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("010")
        .serviceName("Infrastructure Damage")
        .description("Public infrastructure has been damaged.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("011")
        .serviceName("Contents Damage")
        .description("A building's contents have been damaged.")
        .metadata("false")
        .type("realtime")
        .group("Loss/Damage").build())
      .add(new ServiceList.Builder().serviceCode("012")
        .serviceName("Hazard")
        .description("A situation that may cause property to be damaged or people to be injured.")
        .metadata("false")
        .type("realtime")
        .group("Hazard/Risk").build())
      .build();
  }}
}
