# Create your models here.
from django.contrib.auth.models import AbstractUser
from django.db import models
from django.utils.timezone import now

class User(AbstractUser):
    username = models.CharField(max_length=50, unique=True)
    password = models.CharField(max_length=50)
    USERNAME_FIELD="username"
    REQUIRED_FIELDS = []

class VehicleType(models.Model):
    name = models.CharField(max_length=50)

class VehicleMerk(models.Model):
    name = models.CharField(max_length=50)

class Vehicle(models.Model):
    user = models.ForeignKey(User, on_delete=models.DO_NOTHING)
    tipe = models.ForeignKey(VehicleType, on_delete=models.DO_NOTHING)
    merk = models.ForeignKey(VehicleMerk, on_delete=models.DO_NOTHING)
    from_name = models.CharField(max_length=25, default="")
    car_name = models.CharField(max_length=25, default="")
    #foto_kendaraan = models.CharField(max_length=25, default="")
    # foto_stnk = models.CharField(max_length=25, default="")
    # foto_kuitansi_servis = models.CharField(max_length=25, default="")
    # foto_ktp = models.CharField(max_length=25, default="")
    foto_foto = models.CharField(max_length=25, default="")
    
    pajak_selanjutnya = models.DateField(default=now)
    servis_selanjutnya = models.DateField(default=now)

class VehiclePhoto(models.Model):
    vehicle = models.ForeignKey(Vehicle, on_delete=models.DO_NOTHING)
    photo = models.CharField(max_length=25, default="")

class ServiceReceipt(models.Model):
    vehicle = models.ForeignKey(Vehicle, on_delete=models.DO_NOTHING)
    photo = models.CharField(max_length=25, default="")    
