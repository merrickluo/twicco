import React from 'react'

import {
  View,
  Text,
  Image,
} from 'react-native'

export default class Tweet extends React.Component {

  render() {
    const tweet = this.props.tweet
    return (
      <View style={styles.container}>
        <Image
          style={styles.avatar}
          source={{ uri: tweet.user.profile_image_url_https }}
        />
        <View style={styles.content}>
          <Text style={styles.mediumText}>{`${tweet.user.name} @${tweet.user.screen_name}`}</Text>
          <Text style={styles.text}>{tweet.text}</Text>
          <Text style={styles.smallText}>via xxx</Text>
        </View>
      </View>
    )
  }
}

const styles = {
  container: {
    flex: 1,
    flexDirection: 'row',
    padding: 8,
  },
  avatar: {
    margin: 4,
    width: 40,
    height: 40,
  },
  content: {
    flex: 1,
    flexDirection: 'column',
    marginLeft: 8,
  },
  mediumText: {
    color: 'white',
    fontSize: 14,
  },
  smallText: {
    color: 'white',
    fontSize: 12,
  },
  text: {
    color: 'white',
    fontSize: 16,
  }
}
