import { useMemo } from 'react';
import { VStack, HStack, Heading, Text, IconButton, Tooltip } from '@chakra-ui/react';
import { faCheckDouble } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import dayjs from 'dayjs';
import useMutation from 'swr/mutation'

import { useSWRConfig } from 'swr';

interface NotificationCardProps {
  item: Notification;
  putter(value: `/notification/unreads/${string}`): Promise<unknown>;
}


export type Notification = {
  created_at: string;
  id: string;
  read: boolean;
  sent: boolean;
  parameters: [];
  name: string;
  code: 'tasks_created' | 'tasks_deleted' |'tasks_finished';
};

const NotificationCard: React.FC<NotificationCardProps> = ({ item, putter }) => {
  const { trigger } = useMutation(`/notification/unreads/${item.id}`, putter);
  const { mutate } = useSWRConfig();
  const date = useMemo(() => dayjs(item.created_at), [item.created_at])

  return (
    <VStack as={'li'} p={2} bg={item.read ? 'gray.100' : 'yellow.100'} alignItems={'stretch'} rounded={6}>
      <HStack alignItems={'flex-start'}>
        <Heading
          flex={1}
          lineHeight={'none'}
          fontFamily={'Montserrat'}
          fontWeight={500}
          size={'sm'}
        >
          {message(item.code) + '\n'}
          <Text fontFamily={'Barlow'}>{item.parameters.filter(item => item['key'] === 'name')
              .map(item => item['value'])}</Text>
        </Heading>
        <Tooltip isDisabled={item.read} hasArrow label={'Mark as read'}>
          <IconButton 
            _hover={{ color: 'green' }}
            color={'gray'}
            aria-label='Mark as read'
            colorScheme={'unstyled'}
            isDisabled={item.read}
            icon={<FontAwesomeIcon color={'inherit'} fontSize={16} icon={faCheckDouble} />}
            p={1}
            size={'xs'}
            onClick={() => {
              trigger().finally(() => mutate('/notification'));
            }}
          />
        </Tooltip>
      </HStack>
      <Tooltip label={date.format('LLLL')}>
        <Text 
          color={'white'} 
          rounded={4} 
          alignSelf={'flex-start'} 
          bg={'gray.600'} 
          px={1} 
          fontFamily={'Barlow'} 
          fontWeight={500} 
          size={'sm'}
          userSelect={'none'}
        >
          {date.calendar(null, {
            sameDay: '[Today at] LT',
            nextDay: '[Tomorrow at] LT',
            nextWeek: 'dddd [at] LT',
            lastDay: '[Yesterday at] LT',
            sameElse: 'DD/MM/YYYY'
          })}
        </Text>
      </Tooltip>
    </VStack>
  );
};

function message(type: Notification['code']) {
  switch (type) {
    case 'tasks_created':
      return 'Task created';
    case 'tasks_deleted':
      return 'Task has been deleted';
    case 'tasks_finished':
      return 'A task has been completed';
  }
}

export default NotificationCard;
