import { ActionReducerMap, MetaReducer } from '@ngrx/store';
import { environment } from '../../../environments/environment';

import { AppState } from '../app.state';
import { authReducer } from './auth.reducer';
import { productReducer } from './product.reducer';
import { cartReducer } from './cart.reducer';

export const reducers: ActionReducerMap<AppState> = {
  auth: authReducer,
  products: productReducer,
  cart: cartReducer
};

export const metaReducers: MetaReducer<AppState>[] = !environment.production ? [] : [];