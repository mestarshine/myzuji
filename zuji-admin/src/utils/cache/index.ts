import {getStorageShortName} from '/@/utils/env';
import {createStorage as CreateStorageParams} from './storageCache';
import {DEFAULT_CACHE_TIME, enableStorageEncryption} from '/@/settings/encryptionSetting';

export type Options = Partial<CreateStorageParams>;

const createOptions = (storage: Storage, options: Options = {}): Options => {
  return {
    // No encryption in debug mode
    hasEncrypt: enableStorageEncryption,
    storage,
    prefixKey: getStorageShortName(),
    ...options,
  };
};

export const createLocalStorage = (options: Options = {}) => {
  return createStorage(localStorage, {...options, timeout: DEFAULT_CACHE_TIME});
};
