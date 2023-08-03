from django.urls import path
from .views import *
    

app_name = 'API'

urlpatterns = [
    path('allvehicles/', get_all_vehicle),
    path('addvehicle/', add_vehicle),
    path('editvehicle/', edit_vehicle),
    path('deletevehicle/', delete_vehicle),
    path('createaccount/', create_account),
    path('test/', test),
    path('checkpajakservis/', checkPajakServis),
]
